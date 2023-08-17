package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabasePoll

@Dao
interface PollsDao : BaseDao<DatabasePoll> {
    /**
     * Updates the user's own votes on a poll
     */
    @Query(
        "UPDATE polls " +
        "SET ownVotes = :choices " +
        "WHERE pollId = :pollId"
    )
    fun updateOwnVotes(pollId: String, choices: List<Int>?)
}