package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

interface BookmarksApi {

    suspend fun getBookmarks(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkStatus>>
}