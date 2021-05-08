package liveEntity

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.User
import dev.kord.core.live.LiveUser
import dev.kord.core.live.onUpdate
import dev.kord.gateway.UserUpdate
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(KordPreview::class)
class LiveUserTest : AbstractLiveEntityTest<LiveUser>() {

    private lateinit var userId: Snowflake

    @BeforeAll
    override fun onBeforeAll() {
        super.onBeforeAll()
        userId = Snowflake(0)
    }

    @BeforeTest
    fun onBefore() = runBlocking {
        live = LiveUser(
            user = User(
                kord = kord,
                data = UserData(
                    id = userId,
                    username = "",
                    discriminator = ""
                )
            )
        )
    }

    @Test
    fun `Check onUpdate is called when event is received`() {
        countdownContext(1) {
            live.onUpdate {
                assertEquals(it.user.id, userId)
                count()
            }

            sendEventValidAndRandomId(userId) {
                UserUpdate(
                    DiscordUser(
                        id = it,
                        username = "",
                        discriminator = "",
                        avatar = null
                    ),
                    0
                )
            }
        }
    }
}