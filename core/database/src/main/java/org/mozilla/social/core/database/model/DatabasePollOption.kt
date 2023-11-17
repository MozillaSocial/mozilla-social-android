package org.mozilla.social.core.database.model

import kotlinx.serialization.Serializable

@Serializable
data class DatabasePollOption(
    /**
     * The label of the poll option.
     */
    val title: String,
    /**
     * The number of received votes for this option.
     *
     * null if results are not yet published.
     */
    val votesCount: Long? = null,
)
