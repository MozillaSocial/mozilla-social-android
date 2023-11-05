package org.mozilla.social.core.ui.common.postcard

import org.mozilla.social.core.ui.common.poll.PollInteractions
import org.mozilla.social.core.ui.common.htmlcontent.HtmlContentInteractions

interface PostCardInteractions : PollInteractions, HtmlContentInteractions {
    fun onReplyClicked(statusId: String) = Unit
    fun onBoostClicked(statusId: String, isBoosting: Boolean) = Unit
    fun onFavoriteClicked(statusId: String, isFavoriting: Boolean) = Unit
    fun onPostCardClicked(statusId: String) = Unit
    fun onOverflowMuteClicked(accountId: String) = Unit
    fun onOverflowBlockClicked(accountId: String) = Unit
    fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    ) = Unit
    fun onOverflowDeleteClicked(statusId: String) = Unit
    fun onAccountImageClicked(accountId: String) = Unit
}