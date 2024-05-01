package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.DbTrendingStatus
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper

@Dao
interface TrendingStatusDao: BaseDao<DbTrendingStatus> {
    @Transaction
    @Query(
        "SELECT * FROM trendingStatuses " +
                "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, TrendingStatusWrapper>

    @Query("DELETE FROM trendingStatuses")
    fun deleteAll()
}
