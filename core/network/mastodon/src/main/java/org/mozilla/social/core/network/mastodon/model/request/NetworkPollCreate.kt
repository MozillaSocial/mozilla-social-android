package org.mozilla.social.core.network.mastodon.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.core.network.mastodon.model.NetworkPoll

/**
 * Object used to create a new [NetworkPoll].
 */
@Serializable
data class NetworkPollCreate(

    /**
     * Array of possible answers.
     */
    @SerialName("options")
    val options: List<String>,

    /**
     * Duration the poll should be open, in seconds.
     */
    @SerialName("expires_in")
    val expiresInSec: Long,

    /**
     * Allow multiple choices in the poll?
     */
    @SerialName("multiple")
    val allowMultipleChoices: Boolean?,

    /**
     * Hide vote counts until the poll ends?
     */
    @SerialName("hide_totals")
    val hideTotals: Boolean?
)
