package org.mozilla.social.core.ui.postcard.poll

interface PollInteractions {
    fun onVoteClicked(pollId: String, choices: List<Int>) = Unit
}