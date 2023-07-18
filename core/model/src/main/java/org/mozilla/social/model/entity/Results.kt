package org.mozilla.social.model.entity

/**
 * Represents the results of a search.
 */
data class Results(

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
    val hashtags: List<Tag>
)
