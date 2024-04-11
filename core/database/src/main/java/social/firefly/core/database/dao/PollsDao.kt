package social.firefly.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import social.firefly.core.database.model.entities.DatabasePoll

@Dao
interface PollsDao : BaseDao<DatabasePoll> {
    /**
     * Updates the user's own votes on a poll
     */
    @Query(
        "UPDATE polls " +
                "SET ownVotes = :choices " +
                "WHERE pollId = :pollId",
    )
    suspend fun updateOwnVotes(
        pollId: String,
        choices: List<Int>?,
    )

    @Query(
        "DELETE FROM polls " +
                "WHERE pollId NOT IN (:pollIdsToKeep)"
    )
    suspend fun deleteAll(
        pollIdsToKeep: List<String> = emptyList()
    )

    @Query(
        "DELETE FROM polls " +
                "WHERE pollId NOT IN " +
                "(" +
                "SELECT pollId FROM statuses" +
                ")"
    )
    suspend fun deleteOldPolls()
}
