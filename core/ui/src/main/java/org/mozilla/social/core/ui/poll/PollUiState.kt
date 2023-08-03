package org.mozilla.social.core.ui.poll

data class PollUiState(
    val pollOptions: List<PollOptionUiState>,
    val pollInfoText: String,
    val isUserCreatedPoll: Boolean,
)

data class PollOptionUiState(
    val clickToVoteEnabled: Boolean,
    val fillFraction: Float,
    val title: String,
    val voteInfo: String,
)