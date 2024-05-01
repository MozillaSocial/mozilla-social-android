package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import social.firefly.core.database.model.entities.TrendingLink
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTagWrapper
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper


@Dao
interface TrendingLinkDao: BaseDao<TrendingLink> {
    @Transaction
    @Query(
        "SELECT * FROM trendingLinks " +
                "ORDER BY position ASC",
    )
    fun linksPagingSource(): PagingSource<Int, TrendingLink>
}