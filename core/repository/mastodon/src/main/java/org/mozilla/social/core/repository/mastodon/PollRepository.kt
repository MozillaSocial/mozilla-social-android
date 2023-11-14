package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.database.datasource.PollLocalDataSource
import org.mozilla.social.core.model.Poll

class PollRepository internal constructor(private val localDataSource: PollLocalDataSource) {
    fun updateOwnVotes(pollId: String, choices: List<Int>?) = localDataSource.updateOwnVotes(
        pollId = pollId,
        choices = choices
    )

    suspend fun deletePoll(pollId: String) = localDataSource.deletePoll(pollId = pollId)
    fun insertAll(polls: List<Poll>) {
        localDataSource.insertAll(polls)
    }

    fun update(poll: Poll) = localDataSource.update(poll)
}