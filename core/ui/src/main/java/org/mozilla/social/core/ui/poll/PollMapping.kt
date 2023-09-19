package org.mozilla.social.core.ui.poll

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.StringFactoryConcatenator
import org.mozilla.social.common.utils.timeLeft
import org.mozilla.social.core.ui.R
import org.mozilla.social.model.Poll
import org.mozilla.social.model.PollOption

/**
 * @param isUserCreatedPoll refers to if the current user is the creator of the poll
 */
fun Poll.toPollUiState(
    isUserCreatedPoll: Boolean,
): PollUiState =
    PollUiState(
        pollOptions = options.map { pollOption ->
            val voteFraction = getVoteFraction(votesCount, pollOption)
            PollOptionUiState(
                fillFraction = voteFraction,
                title = pollOption.title,
                voteInfo = getVoteCountText(pollOption, voteFraction),
            )
        },
        isUserCreatedPoll = isUserCreatedPoll,
        pollInfoText = StringFactoryConcatenator(
            StringFactory.quantityString(
                R.plurals.vote_count,
                votesCount.toInt(),
                votesCount.toInt(),
            ),
            " - ",
            expiresAt?.timeLeft() ?: StringFactory.string(R.string.poll_closed),
            if (allowsMultipleChoices) {
                StringFactoryConcatenator(
                    " - ",
                    StringFactory.string(R.string.poll_choose_one_or_more_options)
                )
            } else ""
        ),
        showResults = hasVoted ?: false,
        pollId = pollId,
        isMultipleChoice = allowsMultipleChoices,
        usersVotes = ownVotes ?: emptyList(),
        isExpired = isExpired,
        canVote = ownVotes.isNullOrEmpty() && !isExpired && !isUserCreatedPoll,
    )

private fun getVoteCountText(
    pollOption: PollOption,
    voteFraction: Float,
): StringFactory {
    val votePercent = (voteFraction * 100).toInt()

    return StringFactory.quantityString(
        R.plurals.vote_count_and_percent,
        pollOption.votesCount?.toInt() ?: 0,
        pollOption.votesCount?.toInt() ?: 0,
        votePercent,
    )
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