package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.model.DatabaseEmoji
import org.mozilla.social.core.database.model.entities.DatabasePoll
import org.mozilla.social.core.database.model.DatabasePollOption
import org.mozilla.social.core.model.Emoji
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.model.PollOption

class PollRepository internal constructor(private val dao: PollsDao) {
    fun updateOwnVotes(
        pollId: String,
        choices: List<Int>?,
    ) = dao.updateOwnVotes(
        pollId = pollId,
        choices = choices,
    )

    suspend fun deletePoll(pollId: String) = dao.deletePoll(pollId = pollId)

    fun insertAll(polls: List<Poll>) {
        dao.upsertAll(polls.map { it.toDatabaseModel() })
    }

    fun insert(poll: Poll) = dao.upsert(poll.toDatabaseModel())

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
