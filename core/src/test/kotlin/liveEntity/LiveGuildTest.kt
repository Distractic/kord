package liveEntity

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.optionalSnowflake
import dev.kord.core.cache.data.GuildData
import dev.kord.core.entity.Guild
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.live.*
import dev.kord.gateway.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import org.junit.jupiter.api.TestInstance
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(KordPreview::class)
class LiveGuildTest : AbstractLiveEntityTest<LiveGuild>() {

    @BeforeTest
    fun onBefore() = runBlocking {
        live = LiveGuild(
            Guild(
                kord = kord,
                data = GuildData(
                    id = guildId,
                    name = "",
                    ownerId = randomId(),
                    region = "",
                    afkTimeout = 0,
                    verificationLevel = VerificationLevel.None,
                    defaultMessageNotifications = DefaultMessageNotificationLevel.AllMessages,
                    explicitContentFilter = ExplicitContentFilter.Disabled,
                    roles = emptyList(),
                    emojis = emptyList(),
                    features = emptyList(),
                    mfaLevel = MFALevel.None,
                    premiumTier = PremiumTier.None,
                    preferredLocale = "",
                    systemChannelFlags = SystemChannelFlags(0)
                )
            )
        )
    }

    @Test
    fun `Check onEmojisUpdate is called when event is received`() {
        countdownContext(1) {
            live.onEmojisUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildEmojisUpdate(
                    DiscordUpdatedEmojis(
                        guildId = it,
                        emojis = emptyList()
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onBanAdd is called when event is received`() {
        countdownContext(1) {
            live.onBanAdd {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildBanAdd(
                    DiscordGuildBan(
                        guildId = it.asString,
                        user = DiscordUser(
                            id = randomId(),
                            username = "",
                            discriminator = "",
                            avatar = null
                        )
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onBanRemove is called when event is received`() {
        countdownContext(1) {
            live.onBanRemove {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildBanRemove(
                    DiscordGuildBan(
                        guildId = it.asString,
                        user = DiscordUser(
                            id = randomId(),
                            username = "",
                            discriminator = "",
                            avatar = null
                        )
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onPresenceUpdate is called when event is received`() {
        countdownContext(1) {
            live.onPresenceUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                PresenceUpdate(
                    DiscordPresenceUpdate(
                        user = DiscordPresenceUser(
                            id = randomId(),
                            details = JsonObject(emptyMap())
                        ),
                        guildId = it.optionalSnowflake(),
                        status = PresenceStatus.DoNotDisturb,
                        activities = emptyList(),
                        clientStatus = DiscordClientStatus()
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onVoiceServerUpdate is called when event is received`() {
        countdownContext(1) {
            live.onVoiceServerUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                VoiceServerUpdate(
                    DiscordVoiceServerUpdateData(
                        guildId = it,
                        token = "",
                        endpoint = null
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onVoiceStateUpdate is called when event is received`() {
        countdownContext(1) {
            live.onVoiceStateUpdate {
                assertEquals(guildId, it.state.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                VoiceStateUpdate(
                    DiscordVoiceState(
                        guildId = it.optionalSnowflake(),
                        channelId = null,
                        userId = randomId(),
                        sessionId = "",
                        deaf = false,
                        mute = false,
                        selfDeaf = false,
                        selfMute = false,
                        selfVideo = false,
                        suppress = false,
                        requestToSpeakTimestamp = null
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onWebhookUpdate is called when event is received`() {
        countdownContext(1) {
            live.onWebhookUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                WebhooksUpdate(
                    DiscordWebhooksUpdateData(
                        guildId = it,
                        channelId = randomId(),
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onRoleCreate is called when event is received`() {
        countdownContext(1) {
            live.onRoleCreate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildRoleCreate(
                    DiscordGuildRole(
                        guildId = it,
                        role = DiscordRole(
                            id = randomId(),
                            name = "",
                            color = 0,
                            hoist = false,
                            position = 0,
                            permissions = Permissions(Permission.BanMembers),
                            managed = false,
                            mentionable = false
                        )
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onRoleUpdate is called when event is received`() {
        countdownContext(1) {
            live.onRoleUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildRoleUpdate(
                    DiscordGuildRole(
                        guildId = it,
                        role = DiscordRole(
                            id = randomId(),
                            name = "",
                            color = 0,
                            hoist = false,
                            position = 0,
                            permissions = Permissions(Permission.BanMembers),
                            managed = false,
                            mentionable = false
                        )
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onRoleDelete is called when event is received`() {
        countdownContext(1) {
            live.onRoleDelete {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildRoleDelete(
                    DiscordDeletedGuildRole(
                        guildId = it,
                        id = randomId()
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onMemberJoin is called when event is received`() {
        countdownContext(1) {
            live.onMemberJoin {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildMemberAdd(
                    DiscordAddedGuildMember(
                        guildId = it,
                        user = Optional.invoke(
                            DiscordUser(
                                id = randomId(),
                                username = "",
                                discriminator = "",
                                avatar = null
                            )
                        ),
                        roles = emptyList(),
                        deaf = false,
                        mute = false,
                        joinedAt = ""
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onMemberUpdate is called when event is received`() {
        countdownContext(1) {
            live.onMemberUpdate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildMemberUpdate(
                    DiscordUpdatedGuildMember(
                        guildId = it,
                        roles = emptyList(),
                        user = DiscordUser(
                            id = randomId(),
                            username = "",
                            discriminator = "",
                            avatar = null
                        ),
                        joinedAt = ""
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onMemberLeave is called when event is received`() {
        countdownContext(1) {
            live.onMemberLeave {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                GuildMemberRemove(
                    DiscordRemovedGuildMember(
                        guildId = it,
                        user = DiscordUser(
                            id = randomId(),
                            username = "",
                            discriminator = "",
                            avatar = null
                        )
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onReactionAdd is called when event is received`() {
        countdownContext(1) {
            val emojiExpected = ReactionEmoji.Unicode("\uD83D\uDC28")

            live.onReactionAdd {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                MessageReactionAdd(
                    MessageReactionAddData(
                        messageId = randomId(),
                        channelId = randomId(),
                        guildId = it.optionalSnowflake(),
                        userId = randomId(),
                        emoji = DiscordPartialEmoji(null, emojiExpected.name)
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onReactionAdd with specific reaction is called when event is received`() {
        countdownContext(1) {
            val emojiExpected = ReactionEmoji.Unicode("\uD83D\uDC28")
            val emojiOther = ReactionEmoji.Unicode("\uD83D\uDC3B")

            live.onReactionAdd(emojiExpected) {
                assertEquals(guildId, it.guildId)
                assertEquals(emojiExpected, it.emoji)
                countDown()
            }

            fun createEvent(guildId: Snowflake, emoji: ReactionEmoji) = MessageReactionAdd(
                MessageReactionAddData(
                    messageId = randomId(),
                    channelId = randomId(),
                    guildId = guildId.optionalSnowflake(),
                    userId = randomId(),
                    emoji = DiscordPartialEmoji(null, emoji.name)
                ),
                0
            )

            val eventRandomId = createEvent(randomId(), emojiExpected)
            sendEvent(eventRandomId)

            val eventOtherReaction = createEvent(guildId, emojiOther)
            sendEvent(eventOtherReaction)

            val event = createEvent(guildId, emojiExpected)
            sendEvent(event)
        }
    }

    @Test
    fun `Check onReactionRemove is called when event is received`() {
        countdownContext(1) {
            val emojiExpected = ReactionEmoji.Unicode("\uD83D\uDC28")

            live.onReactionRemove {
                assertEquals(guildId, it.guildId)
                assertEquals(emojiExpected, it.emoji)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                MessageReactionRemove(
                    MessageReactionRemoveData(
                        messageId = randomId(),
                        channelId = randomId(),
                        guildId = it.optionalSnowflake(),
                        userId = randomId(),
                        emoji = DiscordPartialEmoji(null, emojiExpected.name)
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onReactionRemove with specific reaction is called when event is received`() {
        countdownContext(1) {
            val emojiExpected = ReactionEmoji.Unicode("\uD83D\uDC28")
            val emojiOther = ReactionEmoji.Unicode("\uD83D\uDC3B")

            live.onReactionRemove(emojiExpected) {
                assertEquals(guildId, it.guildId)
                assertEquals(emojiExpected, it.emoji)
                countDown()
            }

            fun createEvent(guildId: Snowflake, emoji: ReactionEmoji) = MessageReactionRemove(
                MessageReactionRemoveData(
                    messageId = randomId(),
                    channelId = randomId(),
                    guildId = guildId.optionalSnowflake(),
                    userId = randomId(),
                    emoji = DiscordPartialEmoji(null, emoji.name)
                ),
                0
            )

            val eventRandomId = createEvent(randomId(), emojiExpected)
            sendEvent(eventRandomId)

            val eventOtherReaction = createEvent(guildId, emojiOther)
            sendEvent(eventOtherReaction)

            val event = createEvent(guildId, emojiExpected)
            sendEvent(event)
        }
    }

    @Test
    fun `Check onReactionRemoveAll is called when event is received`() {
        countdownContext(1) {
            live.onReactionRemoveAll {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                MessageReactionRemoveAll(
                    AllRemovedMessageReactions(
                        channelId = randomId(),
                        messageId = randomId(),
                        guildId = it.optionalSnowflake()
                    ),
                    0
                )
            }
        }
    }

    @Test
    fun `Check onMessageCreate is called when event is received`() {
        countdownContext(1) {
            live.onMessageCreate {
                assertEquals(guildId, it.guildId)
                countDown()
            }

            sendEventValidAndRandomId(guildId) {
                MessageCreate(
                    DiscordMessage(
                        id = randomId(),
                        channelId = randomId(),
                        guildId = it.optionalSnowflake(),
                        author = DiscordUser(
                            id = randomId(),
                            username = "",
                            discriminator = "",
                            avatar = null
                        ),
                        content = "",
                        timestamp = "",
                        editedTimestamp = null,
                        tts = false,
                        mentionEveryone = false,
                        mentions = emptyList(),
                        mentionRoles = emptyList(),
                        attachments = emptyList(),
                        embeds = emptyList(),
                        pinned = false,
                        type = MessageType.Default
                    ),
                    0
                )
            }
        }
    }
}