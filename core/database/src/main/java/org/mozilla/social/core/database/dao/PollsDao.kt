package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.DatabasePoll

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
    fun updateOwnVotes(
        pollId: String,
        choices: List<Int>?,
    )

    @Query(
        "DELETE FROM polls " +
            "WHERE pollId = :pollId",
    )
    suspend fun deletePoll(pollId: String)

    @Transaction
    @Query(
        "SELECT * FROM polls " +
            "WHERE pollId = :pollId",
    )
    suspend fun getPoll(pollId: String): DatabasePoll

    @Query(
        "DELETE FROM polls " +
        "WHERE pollId NOT IN (:pollIdsToKeep)"
    )
    suspend fun deleteAll(
        pollIdsToKeep: List<String> = emptyList()
    )
}
