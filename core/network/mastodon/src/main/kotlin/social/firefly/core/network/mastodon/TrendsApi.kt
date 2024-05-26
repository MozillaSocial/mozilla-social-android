package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag
import social.firefly.core.network.mastodon.model.responseBody.NetworkLink
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

interface TrendsApi {

    suspend fun getTrendingTags(
        limit: Int? = null,
        offset: Int? = null,
    ): List<NetworkHashTag>

    suspend fun getTrendingStatuses(
        limit: Int? = null,
        offset: Int? = null,
    ): List<NetworkStatus>

    suspend fun getTrendingLinks(
        limit: Int? = null,
        offset: Int? = null,
    ): List<NetworkLink>
}