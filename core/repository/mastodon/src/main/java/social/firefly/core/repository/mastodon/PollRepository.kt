package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.PollsDao
import social.firefly.core.database.model.DatabaseEmoji
import social.firefly.core.database.model.DatabasePollOption
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.model.Emoji
import social.firefly.core.model.Poll
import social.firefly.core.model.PollOption
import social.firefly.core.repository.mastodon.model.poll.toDatabaseModel

class PollRepository internal constructor(private val dao: PollsDao) {

    suspend fun updateOwnVotes(
        pollId: String,
        choices: List<Int>?,
    ) = dao.updateOwnVotes(
        pollId = pollId,
        choices = choices,
    )

    suspend fun insertAll(polls: List<Poll>) {
        dao.upsertAll(polls.map { it.toDatabaseModel() })
    }

    suspend fun insert(poll: Poll) = dao.upsert(poll.toDatabaseModel())

    suspend fun deleteAll(
        pollIdsToKeep: List<String> = emptyList()
    ) = dao.deleteAll(pollIdsToKeep)

    suspend fun deleteOldPolls() = dao.deleteOldPolls()
}
