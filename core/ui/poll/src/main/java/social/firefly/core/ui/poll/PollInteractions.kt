package social.firefly.core.ui.poll

interface PollInteractions {
    fun onVoteClicked(
        pollId: String,
        choices: List<Int>,
    ) = Unit
}
