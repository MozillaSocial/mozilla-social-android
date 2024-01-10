package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotification
import org.mozilla.social.core.database.model.wrappers.NotificationWrapper

@Dao
interface NotificationsDao: BaseDao<DatabaseNotification> {
    @Transaction
    @Query(
        "SELECT * FROM notifications " +
        "ORDER BY createdAt DESC",
    )
    fun notificationsPagingSource(): PagingSource<Int, NotificationWrapper>

    @Query(
        "DELETE FROM notifications"
    )
    suspend fun deleteAll()

    @Upsert
    suspend fun insertAllIntoMainNotificationList(
        mainNotifications: List<MainNotification>
    )

    @Query(
        "DELETE FROM mainNotifications"
    )
    suspend fun deleteMainNotificationsList()
}