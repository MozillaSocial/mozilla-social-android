package social.firefly.core.repository.mastodon.model.poll

import social.firefly.core.database.model.DatabaseEmoji
import social.firefly.core.database.model.DatabasePollOption
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.model.Emoji
import social.firefly.core.model.Poll
import social.firefly.core.model.PollOption

fun Poll.toDatabaseModel(): DatabasePoll =
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