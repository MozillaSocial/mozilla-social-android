package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.PollsDao
import social.firefly.core.database.model.DatabaseEmoji
import social.firefly.core.database.model.DatabasePollOption
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.model.Emoji
import social.firefly.core.model.Poll
import social.firefly.core.model.PollOption

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

private fun Poll.toDatabaseModel(): DatabasePoll =
    DatabasePoll(
        pollId = pollId,
        isExpired = isExpired,
        allowsMultipleChoices = allowsMultipleChoices,
        votesCount = votesCount,
        options = options.map { it.toDatabaseModel() },
        emojis = emojis.map { it.toDatabaseModel() },
        expiresAt = expiresAt,
        votersCount = votersCount,
        hasVoted = hasVoted,
        ownVotes = ownVotes,
    )

fun Emoji.toDatabaseModel(): DatabaseEmoji =
    DatabaseEmoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category,
    )

fun PollOption.toDatabaseModel(): DatabasePollOption =
    DatabasePollOption(
        title = title,
        votesCount = votesCount,
    )
