package social.firefly.feature.post.poll

interface PollInteractions {
    fun onNewPollClicked() = Unit

    fun onPollOptionTextChanged(
        optionIndex: Int,
        text: String,
    ) = Unit

    fun onPollOptionDeleteClicked(optionIndex: Int) = Unit

    fun onAddPollOptionClicked() = Unit

    fun onPollDurationSelected(pollDuration: PollDuration) = Unit

    fun onPollStyleSelected(style: PollStyle) = Unit

    fun onHideCountUntilEndClicked() = Unit
}
