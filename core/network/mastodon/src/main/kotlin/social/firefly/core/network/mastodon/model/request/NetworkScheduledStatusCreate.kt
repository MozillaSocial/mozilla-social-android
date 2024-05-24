package social.firefly.core.network.mastodon.model.request

import kotlinx.datetime.Instant
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility

/**
 * Object used to schedule a status.
 */
data class NetworkScheduledStatusCreate(
    /**
     * Date at which the status will be sent.
     *
     * Must be at least 5 minutes in the future.
     */
    val scheduledAt: Instant,
    /**
     * Text content of the status.
     *
     * If [mediaIds] is provided, this becomes optional.
     * Attaching a [poll] is optional while [status] is provided.
     */
    val status: String? = null,
    /**
     * Array of attachment ids to be attached as media.
     *
     * If provided, [status] becomes optional, and [poll] cannot be used.
     */
    val mediaIds: List<String>? = null,
    val poll: NetworkPollCreate? = null,
    /**
     * ID of the status being replied to, if status is a reply.
     */
    val inReplyToId: String? = null,
    /**
     * Mark status and attached media as sensitive?
     */
    val isSensitive: Boolean? = null,
    /**
     * Text to be shown as a warning or subject before the actual content.
     *
     * Statuses are generally collapsed behind this field.
     */
    val contentWarningText: String? = null,
    /**
     * Visibility of the posted status.
     */
    val visibility: social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility? = null,
    /**
     * ISO 639-1 language code for this status.
     */
    val language: String? = null,
)
