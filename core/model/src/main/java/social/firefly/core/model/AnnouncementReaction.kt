package social.firefly.core.model

/**
 * Represents an emoji reaction to an [Announcement].
 */
data class AnnouncementReaction(
    /**
     * The emoji used for the reaction.
     *
     * Either a unicode emoji, or a custom emoji's shortcode.
     */
    val name: String,
    /**
     * The total number of users who have added this reaction.
     */
    val count: Long,
    /**
     * Whether the authorized user has added this reaction to the announcement.
     */
    val me: Boolean,
    /**
     * URL to the custom emoji.
     */
    val url: String? = null,
    /**
     * URL to a non-animated version of the custom emoji.
     */
    val staticUrl: String? = null,
)
