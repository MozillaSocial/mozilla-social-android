package social.firefly.core.model

import social.firefly.core.model.wrappers.DetailedAccountWrapper

/**
 * Represents the results of a search.
 */
data class SearchResult(
    /**
     * Accounts which matched the query.
     */
    val accounts: List<social.firefly.core.model.Account>,
    /**
     * Statuses which matched the query.
     */
    val statuses: List<Status>,
    /**
     * Hashtags which matched the query.
     */
    val hashtags: List<HashTag>,
)

data class SearchResultDetailed(
    /**
     * Accounts which matched the query.
     */
    val accounts: List<DetailedAccountWrapper>,
    /**
     * Statuses which matched the query.
     */
    val statuses: List<Status>,
    /**
     * Hashtags which matched the query.
     */
    val hashtags: List<HashTag>,
)
