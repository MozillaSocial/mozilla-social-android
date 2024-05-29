package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.database.dao.NotificationsDao
import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotification
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotificationWrapper
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.database.model.entities.notificationCollections.MainNotificationWrapper
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotification
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotificationWrapper
import social.firefly.core.model.Notification
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.NotificationsApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.notifications.toDatabase
import social.firefly.core.repository.mastodon.model.notifications.toExternal

class NotificationsRepository(
    val api: NotificationsApi,
    val dao: NotificationsDao,
) {

    suspend fun getNotifications(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
        types: List<String>? = null,
        excludeTypes: List<String>? = null,
        accountId: String? = null,
    ): MastodonPagedResponse<Notification> = api.getNotifications(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
        types = types,
        excludeTypes = excludeTypes,
        accountId = accountId
    ).toMastodonPagedResponse { it.toExternal() }

    fun getMainPagingSource(): PagingSource<Int, MainNotificationWrapper> = dao.mainNotificationsPagingSource()

    @ExperimentalPagingApi
    fun getMentionListNotificationsPager(
        remoteMediator: RemoteMediator<Int, MentionListNotificationWrapper>,
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
            dao.mentionListNotificationsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.notificationWrapper.toExternal()
            }
        }

    fun getFollowListPagingSource(): PagingSource<Int, FollowListNotificationWrapper> =
        dao.followListNotificationsPagingSource()

    @PreferUseCase
    suspend fun insertAll(
        notifications: List<Notification>
    ) = dao.upsertAll(notifications.mapNotNull { it.toDatabase() })

    suspend fun insertAllMainNotifications(
        mainNotifications: List<MainNotification>
    ) = dao.insertAllIntoMainNotificationList(mainNotifications)

    suspend fun insertMentionNotifications(
        notifications: List<MentionListNotification>
    ) = dao.insertAllIntoMentionsNotificationList(notifications)

    suspend fun insertFollowNotifications(
        notifications: List<FollowListNotification>
    ) = dao.insertAllIntoFollowNotificationList(notifications)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun deleteMainNotificationsList() = dao.deleteMainNotificationsList()

    suspend fun deleteMentionNotificationsList() = dao.deleteMentionNotificationsList()

    suspend fun deleteFollowNotificationsList() = dao.deleteFollowNotificationsList()

    suspend fun deleteOldNotifications() = dao.deleteOldNotifications()

    suspend fun changeNotificationTypeToFollow(
        notificationId: Int,
    ) = dao.changeNotificationType(
        notificationId = notificationId,
        type = DatabaseNotification.Type.FOLLOW,
    )

    suspend fun changeNotificationTypeToFollowRequest(
        notificationId: Int,
    ) = dao.changeNotificationType(
        notificationId = notificationId,
        type = DatabaseNotification.Type.FOLLOW_REQUEST,
    )

    suspend fun deleteNotification(
        notificationId: Int,
    ) = dao.deleteNotification(notificationId)

    suspend fun getNotification(
        notificationId: Int,
    ): Notification = dao.getNotification(notificationId).toExternal()
}