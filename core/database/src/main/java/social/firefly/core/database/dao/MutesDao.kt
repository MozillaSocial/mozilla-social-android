package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper

@Dao
interface MutesDao : BaseDao<DatabaseMute> {
    @Transaction
    @Query(
        "SELECT * FROM mutes " +
                "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, MuteWrapper>

    @Query(
        "DELETE FROM mutes"
    )
    suspend fun deleteAll()
}