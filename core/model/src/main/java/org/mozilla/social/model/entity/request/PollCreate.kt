package org.mozilla.social.model.entity.request

import org.mozilla.social.model.entity.Poll
import org.mozilla.social.model.entity.PollOption

/**
 * Object used to create a new [Poll].
 */
data class PollCreate(

    /**
     * Array of possible answers.
     */
    val options: List<PollOption>,

    /**
     * Duration the poll should be open, in seconds.
     */
    val expiresInSec: Long,

    /**
     * Allow multiple choices in the poll?
     */
    val allowMultipleChoices: Boolean?,

    /**
     * Hide vote counts until the poll ends?
     */
    val hideTotals: Boolean?
)
