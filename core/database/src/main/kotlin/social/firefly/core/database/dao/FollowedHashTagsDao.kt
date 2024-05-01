package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTagWrapper

@Dao
interface FollowedHashTagsDao: BaseDao<FollowedHashTag> {

    @Transaction
    @Query(
        "SELECT * FROM followedHashTags " +
        "ORDER BY position ASC",
    )
    fun pagingSource(): PagingSource<Int, FollowedHashTagWrapper>

    @Query("DELETE FROM followedHashTags")
    fun deleteAll()
}