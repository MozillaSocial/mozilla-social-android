package org.mozilla.social.core.ui.poll

data class PollUiState(
    val pollOptions: List<PollOptionUiState>,
    val pollInfoText: String,
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
    val voteInfo: String,
)