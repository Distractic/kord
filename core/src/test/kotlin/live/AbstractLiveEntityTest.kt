package live

import dev.kord.cache.api.DataCache
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.ClientResources
import dev.kord.core.Kord
import dev.kord.core.builder.kord.Shards
import dev.kord.core.gateway.MasterGateway
import dev.kord.core.live.AbstractLiveKordEntity
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.gateway.*
import dev.kord.rest.request.KtorRequestHandler
import dev.kord.rest.service.RestClient
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.time.Clock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.time.Duration

@OptIn(PrivilegedIntent::class, KordPreview::class)
abstract class AbstractLiveEntityTest<LIVE : AbstractLiveKordEntity> {

    object GatewayMock : Gateway {
        override val coroutineContext: CoroutineContext = EmptyCoroutineContext + SupervisorJob()

        @OptIn(FlowPreview::class)
        override val events: MutableSharedFlow<Event> = MutableSharedFlow()

        override val ping: StateFlow<Duration?> = MutableStateFlow(null)

        override suspend fun detach() {}

        override suspend fun send(command: Command) {}

        override suspend fun start(configuration: GatewayConfiguration) {}

        override suspend fun stop() {}
    }

    protected lateinit var kord: Kord

    protected lateinit var guildId: Snowflake

    protected lateinit var live: LIVE

    @BeforeAll
    open fun onBeforeAll() = runBlocking {
        kord = createKord()
        guildId = Snowflake(2)
    }

    @AfterAll
    open fun onAfterAll() = runBlocking {
        if (kord.isActive) {
            kord.logout()
            kord.shutdown()
        }
    }

    @AfterTest
    open fun onAfter() {
        if (this::live.isInitialized && live.isActive) {
            live.shutdown()
        }
    }

    protected open fun createKord(): Kord = Kord(
        resources = ClientResources("token", Shards(1), HttpClient(), EntitySupplyStrategy.cache, Intents.none),
        cache = DataCache.none(),
        MasterGateway(mapOf(0 to GatewayMock)),
        RestClient(KtorRequestHandler("token", clock = Clock.systemUTC())),
        Snowflake("420"),
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE),
        Dispatchers.Default
    )

    protected inline fun countdownContext(
        initialCount: Int,
        expectedCount: Long = 0,
        waitMs: Long = 1000,
        crossinline action: suspend CountDownLatch.() -> Unit
    ) = runBlocking {
        val countdown = CountDownLatch(initialCount)

        action(countdown)

        countdown.await(waitMs, TimeUnit.MILLISECONDS)
        assertEquals(expectedCount, countdown.count)
    }

    fun randomId() = Snowflake(Random.nextLong())

    protected suspend fun sendEvent(event: Event) = GatewayMock.events.emit(event)
}