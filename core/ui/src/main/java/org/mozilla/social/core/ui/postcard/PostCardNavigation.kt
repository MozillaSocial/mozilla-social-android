package org.mozilla.social.core.ui.postcard

interface PostCardNavigation {
    fun onReplyClicked(statusId: String) = Unit
    fun onPostClicked(statusId: String) = Unit
    fun onReportClicked(accountId: String, statusId: String) = Unit
    fun onAccountClicked(accountId: String) = Unit
}