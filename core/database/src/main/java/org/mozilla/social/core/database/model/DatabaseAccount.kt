package org.mozilla.social.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Represents a user of Mastodon and their associated profile.
 */
@Serializable
@Entity(tableName = "accounts")
data class DatabaseAccount(
    @PrimaryKey
    val accountId: String,

    /**
     * The username of the account, not including the domain.
     */
    val username: String,

    /**
     * The WebFinger account URI.
     *
     * Equal to [username] for local users, or `username@domain` for remote users.
     */
    val acct: String,

    /**
     * The location of the user's profile page.
     */
    val url: String,
    val displayName: String,

    /**
     * The profile's bio / description.
     */
    val bio: String,

    /**
     * URL to an image that is shown next to the account's statuses and on its profile.
     */
    val avatarUrl: String,

    /**
     * URL to a static version of the avatar.
     *
     * Equal to [avatarUrl] if its value is a static image; different if avatar is an animated GIF.
     */
    val avatarStaticUrl: String,

    /**
     * URL to an image banner that is shown above the profile and in profile cards.
     */
    val headerUrl: String,

    /**
     * URL to a static version of the header.
     *
     * Equal to [headerUrl] if its value is a static image; different if avatar is an animated GIF.
     */
    val headerStaticUrl: String,

    /**
     * Whether the account manually approves follow requests.
     */
    val isLocked: Boolean,

    /**
     * Custom emoji entities to be used when rendering the profile.
     *
     * If none, an empty array will be returned.
     */
//    @Embedded(prefix = "emoji_")
    val emojis: List<DatabaseEmoji>,

    /**
     * Date at which the account was created.
     */
    val createdAt: Instant,

    /**
     * Date at which the last status was posted.
     */
    val lastStatusAt: LocalDate? = null,

    /**
     * Total number of statuses posted.
     */
    val statusesCount: Long,

    /**
     * Total number of accounts following this account.
     */
    val followersCount: Long,

    /**
     * Total number of accounts followed by this account.
     */
    val followingCount: Long,

    /**
     * Whether the account has opted into discovery features such as the profile directory.
     */
    val isDiscoverable: Boolean? = null,

    /**
     * Indicates that the profile is currently inactive and that its user has moved to a new account.
     */
//    @Ignore
//    val movedTo: DatabaseAccount? = null,

    /**
     * Whether this account represents a group.
     */
    val isGroup: Boolean,

    /**
     * Additional metadata attached to a profile as name-value pairs.
     */
//    @Embedded(prefix = "field_")
    val fields: List<DatabaseField>? = null,

    /**
     * Whether this account is a robot.
     *
     * Indicates that the account may perform automated actions,
     * may not be monitored, or identifies as a robot.
     */
    val isBot: Boolean? = null,

    /**
     * An entity to be used with API methods to verify and update credentials.
     */
    @Embedded(prefix = "source_")
    val source: DatabaseSource? = null,

    /**
     * Whether the account is suspended.
     */
    val isSuspended: Boolean? = null,

    /**
     * Instant when a timed mute will expire, if applicable.
     */
    val muteExpiresAt: Instant? = null
)
