package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.database.dao.NotificationsDao
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotificationWrapper
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.model.paging.NotificationsPagingWrapper
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.core.network.mastodon.NotificationsApi
import org.mozilla.social.core.network.mastodon.model.NetworkNotification
import org.mozilla.social.core.repository.mastodon.model.notifications.toDatabase
import org.mozilla.social.core.repository.mastodon.model.notifications.toExternal
import retrofit2.HttpException

class NotificationsRepository(
    private val api: NotificationsApi,
    private val dao: NotificationsDao,
    private val accountApi: AccountApi,
) {

    suspend fun getNotifications(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
        types: Array<String>? = null,
        excludeTypes: Array<String>? = null,
        accountId: String? = null,
    ): NotificationsPagingWrapper {

        val response = api.getNotifications(
            maxId = maxId,
            sinceId = sinceId,
            minId = minId,
            limit = limit,
            types = types,
            excludeTypes = excludeTypes,
            accountId = accountId
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val relationships = response.body()
            ?.filter { it is NetworkNotification.Follow || it is NetworkNotification.FollowRequest }
            ?.map { it.account.accountId }
            ?.distinct()
            ?.let { accountApi.getRelationships(it.toTypedArray()) }
            ?: emptyList()

        return NotificationsPagingWrapper(
            notifications = response.body()?.map { it.toExternal(relationships) } ?: emptyList(),
            pagingLinks = response.headers().get("link")?.parseMastodonLinkHeader(),
        )
    }

    @ExperimentalPagingApi
    fun getMainNotificationsPager(
        remoteMediator: RemoteMediator<Int, MainNotificationWrapper>,
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
            dao.mainNotificationsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.notificationWrapper.toExternal()
            }
        }

    @PreferUseCase
    suspend fun insertAll(
        notifications: List<Notification>
    ) = dao.upsertAll(notifications.map { it.toDatabase() })

    suspend fun insertAllMainNotifications(
        mainNotifications: List<MainNotification>
    ) = dao.insertAllIntoMainNotificationList(mainNotifications)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun deleteMainNotificationsList() = dao.deleteMainNotificationsList()

    suspend fun deleteOldNotifications() = dao.deleteOldNotifications()
}