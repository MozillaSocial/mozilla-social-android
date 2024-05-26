package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkNotification

interface NotificationsApi {

    suspend fun getNotifications(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
        types: List<String>? = null,
        excludeTypes: List<String>? = null,
        accountId: String? = null,
    ): Response<List<NetworkNotification>>
}