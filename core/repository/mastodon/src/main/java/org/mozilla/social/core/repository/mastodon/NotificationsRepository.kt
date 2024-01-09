package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.network.mastodon.NotificationsApi

class NotificationsRepository(
    val api: NotificationsApi,
) {

    suspend fun getNotifications(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
        types: Array<String>? = null,
        excludeTypes: Array<String>? = null,
        accountId: String? = null,
    ) = api.getNotifications(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
        types = types,
        excludeTypes = excludeTypes,
        accountId = accountId
    )
}