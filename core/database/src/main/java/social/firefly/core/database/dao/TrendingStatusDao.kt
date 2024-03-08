package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.TrendingStatus
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper

@Dao
interface TrendingStatusDao: BaseDao<TrendingStatus> {
    @Transaction
    @Query(
        "SELECT * FROM trendingStatuses " +
                "ORDER BY position ASC",
    )
    fun statusesPagingSource(): PagingSource<Int, TrendingStatusWrapper>
}
