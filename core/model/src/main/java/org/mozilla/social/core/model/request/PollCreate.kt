package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Poll

/**
 * Object used to create a new [Poll].
 */
data class PollCreate(

    /**
     * Array of possible answers.
     */
    val options: List<String>,

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
