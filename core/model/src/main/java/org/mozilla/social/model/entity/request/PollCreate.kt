package org.mozilla.social.model.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mozilla.social.model.entity.Poll
import org.mozilla.social.model.entity.PollOption

/**
 * Object used to create a new [Poll].
 */
@Serializable
data class PollCreate(

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
