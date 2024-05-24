package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.datetime.Instant

/**
 * Parameters for a [NetworkScheduledStatus].
 */
data class NetworkScheduledStatusParams(
    /**
     * Plain-text content of the status.
     */
    val plainText: String,
    /**
     * Visibility of this status.
     */
    val visibility: social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility,
    /**
     * The ID of the application that created this status.
     */
    val applicationId: String,
    /**
     * ID of the status being replied to, if status is a reply.
     */
    val inReplyToId: String? = null,
    /**
     * Array of attachment ids to be attached as media.
     */
    val mediaIds: List<String>? = null,
    /**
     * Is this status marked as sensitive content?
     */
    val isSensitive: Boolean? = null,
    /**
     * Subject or summary line, below which status content is collapsed until expanded.
     */
    val contentWarningText: String? = null,
    val scheduledAt: Instant? = null,
)
