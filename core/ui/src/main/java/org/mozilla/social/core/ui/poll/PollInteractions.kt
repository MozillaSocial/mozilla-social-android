package org.mozilla.social.core.ui.poll

import org.mozilla.social.model.PollOption

interface PollInteractions {
    fun onOptionClicked(pollOption: PollOption) = Unit
}