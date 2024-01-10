package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.DatabaseNotification
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
}