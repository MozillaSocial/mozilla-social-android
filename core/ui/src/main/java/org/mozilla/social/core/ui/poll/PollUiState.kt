package org.mozilla.social.core.ui.poll

data class PollUiState(
    val pollOptions: List<PollOptionUiState>,
    val pollInfoText: String,
    val isUserCreatedPoll: Boolean,
    val showResults: Boolean,
    val pollId: String,
    val isMultipleChoice: Boolean,
    val usersVotes: List<Int>,
    val voteButtonEnabled: Boolean,
)

data class PollOptionUiState(
    val enabled: Boolean,
    val fillFraction: Float,
    val title: String,
    val voteInfo: String,
    val selected: Boolean,
)