package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

interface TimelineApi {

    suspend fun getHomeTimeline(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkStatus>>

    suspend fun getPublicTimeline(
        localOnly: Boolean? = null,
        federatedOnly: Boolean? = null,
        mediaOnly: Boolean? = null,
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkStatus>>

    suspend fun getHashTagTimeline(
        hashTag: String,
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): Response<List<NetworkStatus>>
}
