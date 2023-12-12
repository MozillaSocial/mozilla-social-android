package org.mozilla.social.core.database.model.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import org.mozilla.social.core.database.model.DatabaseApplication
import org.mozilla.social.core.database.model.DatabaseAttachment
import org.mozilla.social.core.database.model.DatabaseCard
import org.mozilla.social.core.database.model.DatabaseEmoji
import org.mozilla.social.core.database.model.DatabaseHashTag
import org.mozilla.social.core.database.model.DatabaseMention
import org.mozilla.social.core.database.model.DatabaseStatusVisibility

/**
 * Represents a status posted by an account.
 */
@Entity(
    tableName = "statuses",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabasePoll::class,
            parentColumns = ["pollId"],
            childColumns = ["pollId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class DatabaseStatus(
    @PrimaryKey
    val statusId: String,
    /**
     * URI of the status used for federation.
     */
    val uri: String,
    /**
     * The time when this status was created.
     */
    val createdAt: Instant,
    /**
     * The account that authored this status.
     */
    val accountId: String,
    /**
     * HTML-encoded status content.
     */
    val content: String,
    /**
     * Visibility of this status.
     */
    val visibility: DatabaseStatusVisibility,
    /**
     * Is this status marked as sensitive content?
     */
    val isSensitive: Boolean,
    /**
     * Subject or summary line, below which status content is collapsed until expanded.
     */
    val contentWarningText: String,
    /**
     * Media that is attached to this status.
     */
    val mediaAttachments: List<DatabaseAttachment>,
    /**
     * Mentions of users within the status content.
     */
    val mentions: List<DatabaseMention>,
    /**
     * Hashtags used within the status content.
     */
    val hashTags: List<DatabaseHashTag>,
    /**
     * Custom emoji to be used when rendering status content.
     */
    val emojis: List<DatabaseEmoji>,
    /**
     * How many boosts this status has received.
     */
    val boostsCount: Long,
    /**
     * How many favourites this status has received.
     */
    val favouritesCount: Long,
    /**
     * How many replies this status has received.
     */
    val repliesCount: Long,
    /**
     * The application used to post this status.
     */
    @Embedded(prefix = "application_")
    val application: DatabaseApplication? = null,
    /**
     * A link to the status's HTML representation.
     */
    val url: String? = null,
    /**
     * ID of the status being replied to.
     */
    val inReplyToId: String? = null,
    /**
     * ID of the account being replied to.
     */
    val inReplyToAccountId: String? = null,
    val inReplyToAccountName: String? = null,
    /**
     * The status being boosted.
     */
    val boostedStatusId: String? = null,
    val boostedStatusAccountId: String? = null,
    val boostedPollId: String? = null,
    /**
     * The poll attached to the status.
     */
    val pollId: String? = null,
    /**
     * Preview card for links included within status content.
     */
    val card: DatabaseCard? = null,
    /**
     * Primary language of this status.
     *
     * ISO 639-1 language two-letter code.
     */
    val language: String? = null,
    /**
     * Plain-text source of a status.
     *
     * Returned instead of [content] when the status is deleted,
     * so the user may redraft from the source text without the
     * client having to reverse-engineer the original text from
     * the HTML content.
     */
    val plainText: String? = null,
    /**
     * Whether the current account has favorited this status.
     */
    val isFavorited: Boolean? = null,
    /**
     * Whether the current account has boosted this status.
     */
    val isBoosted: Boolean? = null,
    /**
     * Whether the current account has muted notifications for this conversation.
     */
    val isMuted: Boolean? = null,
    /**
     * Whether the current account has bookmarked this status.
     */
    val isBookmarked: Boolean? = null,
    /**
     * Whether the current account has pinned this status.
     *
     * Only appears if the status is pinnable.
     */
    val isPinned: Boolean? = null,
    /**
     * if the status is currently being deleted by the user
     */
    @ColumnInfo(defaultValue = "false")
    val isBeingDeleted: Boolean = false,
)
