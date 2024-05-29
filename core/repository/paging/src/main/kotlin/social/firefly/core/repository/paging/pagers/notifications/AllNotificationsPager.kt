package social.firefly.core.repository.paging.pagers.notifications

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.database.model.entities.notificationCollections.MainNotificationWrapper
import social.firefly.core.model.Notification
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.mastodon.model.notifications.toExternal
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase

class AllNotificationsPager(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
) : IdBasedPager<Notification, MainNotificationWrapper> {

    override fun mapDbObjectToExternalModel(dbo: MainNotificationWrapper): Notification =
        dbo.notificationWrapper.toExternal()

    override suspend fun saveLocally(items: List<PageItem<Notification>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                notificationsRepository.deleteMainNotificationsList()
            }

            saveNotificationsToDatabase(items.map { it.item })
            notificationsRepository.insertAllMainNotifications(
                items.map {
                    MainNotification(
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
        excludeTypes = listOf(
            "admin.sign_up",
            "admin.report",
            "severed_relationships",
        ),
    )

    override fun pagingSource(): PagingSource<Int, MainNotificationWrapper> =
        notificationsRepository.getMainPagingSource()
}