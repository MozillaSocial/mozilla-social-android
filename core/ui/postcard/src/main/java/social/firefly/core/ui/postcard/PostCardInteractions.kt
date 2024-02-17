package social.firefly.core.ui.postcard

import social.firefly.core.model.Attachment
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions
import social.firefly.core.ui.poll.PollInteractions

interface PostCardInteractions : PollInteractions, HtmlContentInteractions {
    fun onReplyClicked(statusId: String) = Unit

    fun onBoostClicked(
        statusId: String,
        isBoosting: Boolean,
    ) = Unit

    fun onFavoriteClicked(
        statusId: String,
        isFavoriting: Boolean,
    ) = Unit

    fun onPostCardClicked(statusId: String) = Unit

    fun onOverflowMuteClicked(
        accountId: String,
        statusId: String,
    ) = Unit

    fun onOverflowBlockClicked(
        accountId: String,
        statusId: String,
    ) = Unit

    fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    ) = Unit

    fun onOverflowDeleteClicked(statusId: String) = Unit

    fun onOverflowEditClicked(statusId: String) = Unit

    fun onAccountImageClicked(accountId: String) = Unit

    fun onMediaClicked(
        attachments: List<Attachment>,
        index: Int,
    ) = Unit
}
