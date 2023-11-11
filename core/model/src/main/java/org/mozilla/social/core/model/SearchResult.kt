package org.mozilla.social.core.model

/**
 * Represents the results of a search.
 */
data class SearchResult(

    /**
     * Accounts which matched the query.
     */
    val accounts: List<Account>,

    /**
     * Statuses which matched the query.
     */
    val statuses: List<Status>,

    /**
     * Hashtags which matched the query.
     */
    val hashtags: List<HashTag>
)
