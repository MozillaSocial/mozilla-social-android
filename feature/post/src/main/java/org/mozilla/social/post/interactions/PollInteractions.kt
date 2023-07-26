package org.mozilla.social.post.interactions

import org.mozilla.social.post.poll.PollDuration

interface PollInteractions {
    fun onNewPollClicked()
    fun onPollOptionTextChanged(optionIndex: Int, text: String)
    fun onPollOptionDeleted(optionIndex: Int)
    fun onAddPollOptionClicked()
    fun onPollDurationChanged(pollDuration: PollDuration)
    fun onPollStyleChanged(multipleChoice: Boolean)
    fun onHideCountUntilEndClicked(enabled: Boolean)
}