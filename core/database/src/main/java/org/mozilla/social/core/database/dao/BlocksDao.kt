package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.accountCollections.BlockWrapper
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseBlock

@Dao
interface BlocksDao: BaseDao<DatabaseBlock> {
    @Transaction
    @Query(
        "SELECT * FROM blocks " +
                "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, BlockWrapper>

    @Query(
        "DELETE FROM blocks"
    )
    suspend fun deleteAll()
}