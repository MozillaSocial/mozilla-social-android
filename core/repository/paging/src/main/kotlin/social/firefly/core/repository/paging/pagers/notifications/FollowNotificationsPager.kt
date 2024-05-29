package social.firefly.core.repository.paging.pagers.notifications

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotification
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotificationWrapper
import social.firefly.core.model.Notification
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.mastodon.model.notifications.toExternal
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase

class FollowNotificationsPager(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
) : IdBasedPager<Notification, FollowListNotificationWrapper> {

    override fun mapDbObjectToExternalModel(dbo: FollowListNotificationWrapper): Notification =
        dbo.notificationWrapper.toExternal()

    override suspend fun saveLocally(items: List<PageItem<Notification>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                notificationsRepository.deleteFollowNotificationsList()
            }

            saveNotificationsToDatabase(items.map { it.item })
            notificationsRepository.insertFollowNotifications(
                items.map {
                    FollowListNotification(
                        id = it.item.id,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(
        limit: Int,
        nextKey: String?
    ): MastodonPagedResponse<Notification> = notificationsRepository.getNotifications(
        maxId = nextKey,
        limit = limit,
        types = listOf(Notification.FollowRequest.VALUE),
    )

    override fun pagingSource(): PagingSource<Int, FollowListNotificationWrapper> =
        notificationsRepository.getFollowListPagingSource()
}