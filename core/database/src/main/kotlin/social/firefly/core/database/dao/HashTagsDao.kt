package social.firefly.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import social.firefly.core.database.model.entities.DatabaseHashTagEntity

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

    @Query(
        "SELECT * FROM hashtags " +
                "WHERE name = :name",
    )
    fun getHashTagFlow(name: String): Flow<DatabaseHashTagEntity>

    @Query(
        "DELETE FROM hashtags"
    )
    suspend fun deleteAll()
}