package org.mozilla.social.core.ui.poll

import org.mozilla.social.common.utils.timeLeft
import org.mozilla.social.model.Poll
import org.mozilla.social.model.PollOption

fun Poll.toPollUiState(): PollUiState =
    PollUiState(
        pollOptions = options.mapIndexed { index, pollOption ->
            val voteFraction = getVoteFraction(votesCount, pollOption)
            PollOptionUiState(
                fillFraction = voteFraction,
                title = pollOption.title,
                voteInfo = getVoteCountText(pollOption, voteFraction),
            )
        },
        isUserCreatedPoll = false,
        pollInfoText = "$votesCount votes - ${expiresAt?.timeLeft() ?: "poll closed"}" +
                if (allowsMultipleChoices) {
                    " - Choose one or more"
                } else "",
        showResults = hasVoted ?: false,
        pollId = pollId,
        isMultipleChoice = allowsMultipleChoices,
        usersVotes = ownVotes ?: emptyList(),
        isExpired = isExpired,
    )

private fun getVoteCountText(
    pollOption: PollOption,
    voteFraction: Float,
): String {
    val voteText = if (pollOption.votesCount == 1L) {
        "vote"
    } else {
        "votes"
    }
    val voteCountText = "${pollOption.votesCount ?: 0} $voteText"

    val votePercent = (voteFraction * 100).toInt()

    return "$voteCountText - $votePercent%"
}

private fun getVoteFraction(
    votesCount: Long,
    pollOption: PollOption,
): Float = if (votesCount == 0L) {
    0f
} else {
    pollOption.votesCount?.let {
        it.toFloat() / votesCount
    } ?: 0f
}