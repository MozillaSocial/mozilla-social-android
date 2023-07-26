package org.mozilla.social.post.interactions

import org.mozilla.social.post.poll.PollDuration
import org.mozilla.social.post.poll.PollStyle

interface PollInteractions {
    fun onNewPollClicked() = Unit
    fun onPollOptionTextChanged(optionIndex: Int, text: String) = Unit
    fun onPollOptionDeleted(optionIndex: Int) = Unit
    fun onAddPollOptionClicked() = Unit
    fun onPollDurationChanged(pollDuration: PollDuration) = Unit
    fun onPollStyleChanged(style: PollStyle) = Unit
    fun onHideCountUntilEndClicked() = Unit
}