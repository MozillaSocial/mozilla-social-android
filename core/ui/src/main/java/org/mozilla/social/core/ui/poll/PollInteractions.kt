package org.mozilla.social.core.ui.poll

interface PollInteractions {
    fun onOptionClicked(optionIndex: Int) = Unit
    fun onVoteClicked() = Unit
}