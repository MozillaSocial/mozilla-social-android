package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user of Mastodon and their associated profile.
 */
@Serializable
data class NetworkAccount(
    @SerialName("id")
    val accountId: String,
    /**
     * The username of the account, not including the domain.
     */
    @SerialName("username")
    val username: String,
    /**
     * The WebFinger account URI.
     *
     * Equal to [username] for local users, or `username@domain` for remote users.
     */
    @SerialName("acct")
    val acct: String,
    /**
     * The location of the user's profile page.
     */
    @SerialName("url")
    val url: String,
    @SerialName("display_name")
    val displayName: String,
    /**
     * The profile's bio / description.
     */
    @SerialName("note")
    val bio: String,
    /**
     * URL to an image that is shown next to the account's statuses and on its profile.
     */
    @SerialName("avatar")
    val avatarUrl: String,
    /**
     * URL to a static version of the avatar.
     *
     * Equal to [avatarUrl] if its value is a static image; different if avatar is an animated GIF.
     */
    @SerialName("avatar_static")
    val avatarStaticUrl: String,
    /**
     * URL to an image banner that is shown above the profile and in profile cards.
     */
    @SerialName("header")
    val headerUrl: String,
    /**
     * URL to a static version of the header.
     *
     * Equal to [headerUrl] if its value is a static image; different if avatar is an animated GIF.
     */
    @SerialName("header_static")
    val headerStaticUrl: String,
    /**
     * Whether the account manually approves follow requests.
     */
    @SerialName("locked")
    val isLocked: Boolean,
    /**
     * Custom emoji entities to be used when rendering the profile.
     *
     * If none, an empty array will be returned.
     */
    @SerialName("emojis")
    val emojis: List<NetworkEmoji>,
    /**
     * Date at which the account was created.
     */
    @SerialName("created_at")
    val createdAt: Instant,
    /**
     * Total number of statuses posted.
     */
    @SerialName("statuses_count")
    val statusesCount: Long,
    /**
     * Total number of accounts following this account.
     */
    @SerialName("followers_count")
    val followersCount: Long,
    /**
     * Total number of accounts followed by this account.
     */
    @SerialName("following_count")
    val followingCount: Long,
    /**
     * Whether the account has opted into discovery features such as the profile directory.
     */
    @SerialName("discoverable")
    val isDiscoverable: Boolean? = null,
    /**
     * Indicates that the profile is currently inactive and that its user has moved to a new account.
     */
    @SerialName("moved")
    val movedTo: NetworkAccount? = null,
    /**
     * Whether this account represents a group.
     */
    @SerialName("group")
    val isGroup: Boolean,
    /**
     * Additional metadata attached to a profile as name-value pairs.
     */
    @SerialName("fields")
    val fields: List<NetworkField>? = null,
    /**
     * Whether this account is a robot.
     *
     * Indicates that the account may perform automated actions,
     * may not be monitored, or identifies as a robot.
     */
    @SerialName("bot")
    val isBot: Boolean? = null,
    /**
     * An entity to be used with API methods to verify and update credentials.
     */
    @SerialName("source")
    val source: NetworkSource? = null,
    /**
     * Whether the account is suspended.
     */
    @SerialName("suspended")
    val isSuspended: Boolean? = null,
    /**
     * Instant when a timed mute will expire, if applicable.
     */
    @SerialName("mute_expires_at")
    val muteExpiresAt: Instant? = null,
)
