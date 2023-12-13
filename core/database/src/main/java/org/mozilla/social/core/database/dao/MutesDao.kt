package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseMute
import org.mozilla.social.core.database.model.entities.accountCollections.MuteWrapper

@Dao
interface MutesDao: BaseDao<DatabaseMute> {
    @Transaction
    @Query(
        "SELECT * FROM mutes " +
                "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, MuteWrapper>

    @Query(
        "DELETE FROM followers " +
                "WHERE accountId = :accountId",
    )
    suspend fun delete(accountId: String)
}