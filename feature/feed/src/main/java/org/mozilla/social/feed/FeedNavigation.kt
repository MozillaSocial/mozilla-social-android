package org.mozilla.social.feed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val FEED_ROUTE = "feed"

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(FEED_ROUTE, navOptions)
}

fun NavGraphBuilder.feedScreen(
    onReplyClicked: (String) -> Unit,
    onPostClicked: (String) -> Unit,
    onReportClicked: (accountId: String, statusId: String) -> Unit,
) {
    composable(route = FEED_ROUTE) {
         FeedScreen(
             onPostClicked = onPostClicked,
             onReplyClicked = onReplyClicked,
             onReportClicked = onReportClicked,
         )
    }
}