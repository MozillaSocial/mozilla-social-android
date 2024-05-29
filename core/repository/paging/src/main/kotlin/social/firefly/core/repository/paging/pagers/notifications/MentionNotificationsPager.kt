package social.firefly.core.repository.paging.pagers.notifications

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotification
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotificationWrapper
import social.firefly.core.model.Notification
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.mastodon.model.notifications.toExternal
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase

class MentionNotificationsPager(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
) : IdBasedPager<Notification, MentionListNotificationWrapper> {

    override fun mapDbObjectToExternalModel(dbo: MentionListNotificationWrapper): Notification =
        dbo.notificationWrapper.toExternal()

    override suspend fun saveLocally(items: List<PageItem<Notification>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                notificationsRepository.deleteMentionNotificationsList()
            }

            saveNotificationsToDatabase(items.map { it.item })
            notificationsRepository.insertMentionNotifications(
                items.map {
                    MentionListNotification(
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
        types = listOf(Notification.Mention.VALUE),
    )

    override fun pagingSource(): PagingSource<Int, MentionListNotificationWrapper> =
        notificationsRepository.getMentionListPagingSource()
}