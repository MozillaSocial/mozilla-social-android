package org.mozilla.social.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the results of a search.
 */
@Serializable
data class SearchResult(

    /**
     * Accounts which matched the query.
     */
    @SerialName("accounts")
    val accounts: List<Account>,

    /**
     * Statuses which matched the query.
     */
    @SerialName("statuses")
    val statuses: List<Status>,

    /**
     * Hashtags which matched the query.
     */
    @SerialName("hashtags")
    val hashtags: List<HashTag>
)
