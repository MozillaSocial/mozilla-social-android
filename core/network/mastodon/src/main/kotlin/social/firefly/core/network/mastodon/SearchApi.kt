package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkSearchResult

interface SearchApi {
    suspend fun search(
        // Search query
        query: String,
        // What to search for.  Possible values: accounts, hashtags, statuses
        type: String? = null,
        // Attempt webfinger lookup?
        resolve: Boolean = false,
        // Only return statuses authored by this account
        accountId: String? = null,
        // Filter out unreviewed tags?  Use true when trying to find trending tags
        excludeUnreviewed: Boolean = false,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        limit: Int? = null,
        // Skip the first n results.
        offset: Int? = null,
    ): NetworkSearchResult
}
