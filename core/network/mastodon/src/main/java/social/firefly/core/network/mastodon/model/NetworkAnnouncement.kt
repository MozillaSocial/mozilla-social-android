package social.firefly.core.network.mastodon.model

import kotlinx.datetime.Instant

/**
 * Represents an announcement set by an administrator.
 */
data class NetworkAnnouncement(
    val announcementId: String,
    /**
     * The content of the announcement.
     */
    val text: String,
    /**
     * Whether the announcement is currently active.
     */
    val isPublished: Boolean,
    /**
     * Whether the announcement has a start/end time.
     */
    val isAllDay: Boolean,
    /**
     * Moment at which the announcement was created.
     */
    val createdAt: Instant,
    /**
     * Moment at which the announcement was last updated.
     */
    val updatedAt: Instant,
    /**
     * Whether the announcement has been read by the user.
     */
    val isRead: Boolean,
    /**
     * Emoji reactions attached to the announcement.
     */
    val reactions: List<NetworkAnnouncementReaction>,
    /**
     * Time at which a scheduled announcement was scheduled.
     */
    val scheduledAt: Instant? = null,
    /**
     * Time at which the announcement will start.
     */
    val startsAt: Instant? = null,
    /**
     * Time at which the announcement will end.
     */
    val endsAt: Instant? = null,
)
