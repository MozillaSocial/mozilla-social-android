package org.mozilla.social.post.poll

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.social.common.utils.edit
import org.mozilla.social.post.interactions.PollInteractions

class PollDelegate : PollInteractions {

    private val _poll = MutableStateFlow<Poll?>(null)
    val poll = _poll.asStateFlow()

    override fun onNewPollClicked() {
        if (poll.value == null) {
            _poll.value = newPoll()
        } else {
            _poll.value = null
        }
    }

    override fun onPollOptionTextChanged(optionIndex: Int, text: String) {
        _poll.edit { this?.copy(
            options = options.toMutableList().apply {
                removeAt(optionIndex)
                add(optionIndex, text)
            }
        ) }
    }

    override fun onPollOptionDeleted(optionIndex: Int) {
        _poll.edit { this?.copy(
            options = options.toMutableList().apply {
                removeAt(optionIndex)
            }
        ) }
    }

    override fun onAddPollOptionClicked() {
        _poll.edit { this?.copy(
            options = options.toMutableList().apply {
                add("")
            }
        ) }
    }

    override fun onPollDurationChanged(pollDuration: PollDuration) {
        _poll.edit { this?.copy(
            pollDuration = pollDuration
        ) }
    }

    override fun onPollStyleChanged(style: PollStyle) {
        _poll.edit { this?.copy(
            style = style
        ) }
    }

    override fun onHideCountUntilEndClicked(enabled: Boolean) {
        _poll.edit { this?.copy(
            hideTotals = enabled
        ) }
    }

    private fun newPoll(): Poll = Poll(
        options = listOf(
            "",
            ""
        ),
        pollDuration = PollDuration.ONE_DAY,
        style = PollStyle.SINGLE_CHOICE,
        hideTotals = false
    )
}