package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A possible choice of answer in a [Poll].
 */
@Serializable
data class PollOption(

    /**
     * The label of the poll option.
     */
    @SerialName("title")
    val title: String,

    /**
     * The number of received votes for this option.
     *
     * null if results are not yet published.
     */
    @SerialName("votes_count")
    val votesCount: Long? = null
)
