package org.mozilla.social.core.network.mastodon.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.core.network.mastodon.model.NetworkStatus
import org.mozilla.social.core.network.mastodon.model.NetworkStatusVisibility

/**
 * Object used to post a new [NetworkStatus].
 */
@Serializable
data class NetworkStatusCreate(

    /**
     * Text content of the status.
     *
     * If [mediaIds] is provided, this becomes optional.
     * Attaching a [poll] is optional while [status] is provided.
     */
    @SerialName("status")
    val status: String? = null,

    /**
     * Array of attachment ids to be attached as media.
     *
     * If provided, [status] becomes optional, and [poll] cannot be used.
     */
    @SerialName("media_ids")
    val mediaIds: List<String>? = null,

    @SerialName("poll")
    val poll: NetworkPollCreate? = null,

    /**
     * ID of the status being replied to, if status is a reply.
     */
    @SerialName("in_reply_to_id")
    val inReplyToId: String? = null,

    /**
     * Mark status and attached media as sensitive?
     */
    @SerialName("sensitive")
    val isSensitive: Boolean? = null,

    /**
     * Text to be shown as a warning or subject before the actual content.
     *
     * Statuses are generally collapsed behind this field.
     */
    @SerialName("spoiler_text")
    val contentWarningText: String? = null,

    /**
     * Visibility of the posted status.
     */
    @SerialName("visibility")
    val visibility: NetworkStatusVisibility? = null,

    /**
     * ISO 639-1 language code for this status.
     */
    @SerialName("language")
    val language: String? = null
)
