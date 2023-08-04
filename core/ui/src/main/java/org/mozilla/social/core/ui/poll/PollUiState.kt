package org.mozilla.social.core.ui.poll

data class PollUiState(
    val pollOptions: List<PollOptionUiState>,
    val pollInfoText: String,
    val isUserCreatedPoll: Boolean,
    val showResults: Boolean,
    val pollId: String,
    val isMultipleChoice: Boolean,
    val usersVotes: List<Int>,
)

data class PollOptionUiState(
    val clickToVoteEnabled: Boolean,
    val fillFraction: Float,
    val title: String,
    val voteInfo: String,
)