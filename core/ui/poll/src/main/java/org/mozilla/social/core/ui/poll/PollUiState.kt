package org.mozilla.social.core.ui.poll

import org.mozilla.social.common.utils.StringFactory

data class PollUiState(
    val pollOptions: List<PollOptionUiState>,
    val pollInfoText: StringFactory,
    val isUserCreatedPoll: Boolean,
    val showResults: Boolean,
    val pollId: String,
    val isMultipleChoice: Boolean,
    val usersVotes: List<Int>,
    val isExpired: Boolean,
    val canVote: Boolean,
)

data class PollOptionUiState(
    val fillFraction: Float,
    val title: String,
    val voteInfo: StringFactory,
)
