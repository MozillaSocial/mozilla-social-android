package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.NotificationsDao
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper
import org.mozilla.social.core.database.model.entities.statusCollections.toStatusWrapper
import org.mozilla.social.core.database.model.wrappers.NotificationWrapper
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.network.mastodon.NotificationsApi
import org.mozilla.social.core.repository.mastodon.model.notifications.toExternal
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

class NotificationsRepository(
    val api: NotificationsApi,
    val dao: NotificationsDao,
) {

    suspend fun getNotifications(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
        types: Array<String>? = null,
        excludeTypes: Array<String>? = null,
        accountId: String? = null,
    ): List<Notification> = api.getNotifications(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
        types = types,
        excludeTypes = excludeTypes,
        accountId = accountId
    ).map { it.toExternal() }

    @ExperimentalPagingApi
    fun getNotificationsPager(
        remoteMediator: RemoteMediator<Int, NotificationWrapper>,
        pageSize: Int = 20,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<Notification>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            dao.notificationsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toExternal()
            }
        }
}