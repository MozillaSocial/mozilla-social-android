package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag

interface FollowedTagsApi {

    suspend fun getFollowedHashTags(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkHashTag>>
}