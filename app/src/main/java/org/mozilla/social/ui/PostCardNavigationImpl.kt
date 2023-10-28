package org.mozilla.social.ui

import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigateTo
import org.mozilla.social.core.ui.postcard.PostCardNavigation

class PostCardNavigationImpl(private val navigateTo: NavigateTo): PostCardNavigation {
    override fun onReplyClicked(statusId: String) {
        navigateTo(NavDestination.NewPost(replyId = statusId))
    }

    override fun onPostClicked(statusId: String) {
        navigateTo(NavDestination.Thread(statusId = statusId))
    }

    override fun onReportClicked(accountId: String, accountHandle: String, statusId: String) {
        navigateTo(NavDestination.Report(
            accountId = accountId,
            accountHandle = accountHandle,
            statusId = statusId
        ))
    }

    override fun onAccountClicked(accountId: String) {
        TODO("Not yet implemented")
    }

    override fun onHashTagClicked(hashTag: String) {
        TODO("Not yet implemented")
    }

    override fun onLinkClicked(url: String) {
        TODO("Not yet implemented")
    }
}