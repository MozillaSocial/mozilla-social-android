package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotification
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotificationWrapper
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.database.model.entities.notificationCollections.MainNotificationWrapper
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotification
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotificationWrapper
import social.firefly.core.database.model.wrappers.NotificationWrapper

@Dao
interface NotificationsDao : BaseDao<DatabaseNotification> {
    @Transaction
    @Query(
        "SELECT * FROM mainNotifications " +
                "ORDER BY id DESC",
    )
    fun mainNotificationsPagingSource(): PagingSource<Int, MainNotificationWrapper>

    @Transaction
    @Query(
        "SELECT * FROM mentionNotifications " +
                "ORDER BY id DESC",
    )
    fun mentionListNotificationsPagingSource(): PagingSource<Int, MentionListNotificationWrapper>

    @Transaction
    @Query(
        "SELECT * FROM followNotifications " +
                "ORDER BY id DESC",
    )
    fun followListNotificationsPagingSource(): PagingSource<Int, FollowListNotificationWrapper>

    @Query(
        "DELETE FROM notifications"
    )
    suspend fun deleteAll()

    @Query(
        "DELETE FROM notifications " +
                "WHERE id NOT IN " +
                "(" +
                "SELECT id FROM mainNotifications " +
                "UNION " +
                "SELECT id FROM mentionNotifications " +
                "UNION " +
                "SELECT id FROM followNotifications" +
                ")"
    )
    suspend fun deleteOldNotifications()

    @Upsert
    suspend fun insertAllIntoMainNotificationList(
        mainNotifications: List<MainNotification>
    )

    @Upsert
    suspend fun insertAllIntoMentionsNotificationList(
        mainNotifications: List<MentionListNotification>
    )

    @Upsert
    suspend fun insertAllIntoFollowNotificationList(
        mainNotifications: List<FollowListNotification>
    )

    @Query(
        "DELETE FROM mainNotifications"
    )
    suspend fun deleteMainNotificationsList()

    @Query(
        "DELETE FROM mentionNotifications"
    )
    suspend fun deleteMentionNotificationsList()

    @Query(
        "DELETE FROM followNotifications"
    )
    suspend fun deleteFollowNotificationsList()

    @Query(
        "UPDATE notifications " +
                "SET type = :type " +
                "WHERE id = :notificationId",
    )
    suspend fun changeNotificationType(
        notificationId: Int,
        type: DatabaseNotification.Type,
    )

    @Query(
        "DELETE FROM notifications " +
                "WHERE id = :notificationId",
    )
    suspend fun deleteNotification(
        notificationId: Int,
    )

    @Query(
        "SELECT * FROM notifications " +
                "WHERE id = :notificationId",
    )
    suspend fun getNotification(
        notificationId: Int,
    ): NotificationWrapper
}