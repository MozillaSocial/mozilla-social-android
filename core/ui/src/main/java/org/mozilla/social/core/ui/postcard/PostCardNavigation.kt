package org.mozilla.social.core.ui.postcard

interface PostCardNavigation {
    fun onReplyClicked(statusId: String)
    fun onPostClicked(statusId: String)
    fun onReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    )
    fun onAccountClicked(accountId: String)
    fun onHashTagClicked(hashTag: String)
    fun onLinkClicked(url: String)
}
