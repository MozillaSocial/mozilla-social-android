package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/api/v2/search")
    suspend fun search(
        // Search query
        @Query("q") query: String,
        // What to search for.  Possible values: accounts, hashtags, statuses
        @Query("type") type: String? = null,
        // Attempt webfinger lookup?
        @Query("resolve") resolve: Boolean = false,
        // Only return statuses authored by this account
        @Query("account_id") accountId: String? = null,
        // Filter out unreviewed tags?  Use true when trying to find trending tags
        @Query("exclude_unreviewed") excludeUnreviewed: Boolean = false,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
        // Skip the first n results.
        @Query("offset") offset: Int? = null,
    ): NetworkSearchResult
}
