package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotificationWrapper
import org.mozilla.social.core.database.model.wrappers.NotificationWrapper

@Dao
interface NotificationsDao: BaseDao<DatabaseNotification> {
    @Transaction
    @Query(
        "SELECT * FROM mainNotifications " +
        "ORDER BY id DESC",
    )
    fun mainNotificationsPagingSource(): PagingSource<Int, MainNotificationWrapper>

    @Query(
        "DELETE FROM notifications"
    )
    suspend fun deleteAll()

    @Query(
        "DELETE FROM notifications " +
        "WHERE id NOT IN " +
        "(" +
            "SELECT id FROM mainNotifications" +
        ")"
    )
    suspend fun deleteOldNotifications()

    @Upsert
    suspend fun insertAllIntoMainNotificationList(
        mainNotifications: List<MainNotification>
    )

    @Query(
        "DELETE FROM mainNotifications"
    )
    suspend fun deleteMainNotificationsList()

    @Query(
        "UPDATE notifications " +
        "SET type = :type " +
        "WHERE id = :notificationId",
    )
    suspend fun changeNotificationType(
        notificationId: String,
        type: DatabaseNotification.Type,
    )

    @Query(
        "DELETE FROM notifications " +
        "WHERE id = :notificationId",
    )
    suspend fun deleteNotification(
        notificationId: String,
    )
}