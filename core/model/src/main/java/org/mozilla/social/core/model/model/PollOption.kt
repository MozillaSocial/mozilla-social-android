package org.mozilla.social.model

/**
 * A possible choice of answer in a [Poll].
 */
data class PollOption(

    /**
     * The label of the poll option.
     */
    val title: String,

    /**
     * The number of received votes for this option.
     *
     * null if results are not yet published.
     */
    val votesCount: Long? = null
)
