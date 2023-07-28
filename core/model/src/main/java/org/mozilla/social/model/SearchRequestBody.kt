package org.mozilla.social.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestBody(
    /**
     * The search query.
     */
    @SerialName("q") val query: String,

    /**
     * Specify whether to search for only accounts, hashtags, statuses
     */
    @SerialName("type") val type: String? = null,

    /**
     * Attempt WebFinger lookup? Defaults to false.
     */
    @SerialName("resolve") val attemptWebFingerLookup: Boolean? = null,

    /**
     * Only include accounts that the user is following? Defaults to false.
     */
    @SerialName("following") val onlyIncludeAccountsIFollow: Boolean? = null,

    /**
     * If provided, will only return statuses authored by this account.
     */
    @SerialName("account_id") val accountId: String? = null,

    /**
     * Filter out unreviewed tags? Defaults to false. Use true when trying to find trending tags.
     */
    @SerialName("exclude_unreviewed") val excludeUnreviewed: Boolean? = null,

    /**
     * Return results older than this ID.
     */
    @SerialName("max_id") val maxId: String? = null,

    /**
     * Return results immediately newer than this ID.
     */
    @SerialName("min_id") val minId: String? = null,

    /**
     * Maximum number of results to return, per type. Defaults to 20 results per category. Max 40 results per category.
     */
    @SerialName("limit") val limit: Int? = null,

    /**
     * Skip the first n results.
     */
    @SerialName("offset") val offset: Int? = null,
)