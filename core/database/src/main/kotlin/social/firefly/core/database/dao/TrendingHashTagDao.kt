package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTagWrapper

@Dao
interface TrendingHashTagDao: BaseDao<TrendingHashTag> {
    @Transaction
    @Query(
        "SELECT * FROM trendingHashTags " +
                "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, TrendingHashTagWrapper>

    @Query("DELETE FROM trendingHashTags")
    fun deleteAll()
}