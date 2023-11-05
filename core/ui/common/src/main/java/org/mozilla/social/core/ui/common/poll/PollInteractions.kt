package org.mozilla.social.core.ui.common.poll

interface PollInteractions {
    fun onVoteClicked(pollId: String, choices: List<Int>) = Unit
}