package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.entities.DatabaseHashTagEntity

@Dao
interface HashTagsDao : BaseDao<DatabaseHashTagEntity> {

    @Query(
        "UPDATE hashtags " +
        "SET following = :isFollowing " +
        "WHERE name = :hashTag",
    )
    suspend fun updateFollowing(
        hashTag: String,
        isFollowing: Boolean,
    )
}