package org.mozilla.social.core.ui.postcard

import org.mozilla.social.core.ui.poll.PollInteractions

interface PostCardInteractions : PollInteractions {
    fun onReplyClicked(statusId: String) = Unit
    fun onBoostClicked() = Unit
    fun onFavoriteClicked() = Unit
    fun onShareClicked() = Unit
}