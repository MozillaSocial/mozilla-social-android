package org.mozilla.social.feed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.ui.postcard.PostCardNavigation

const val FEED_ROUTE = "feed"

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(FEED_ROUTE, navOptions)
}

fun NavGraphBuilder.feedScreen(
    postCardNavigation: PostCardNavigation,
) {
    composable(route = FEED_ROUTE) {
        FeedScreen(postCardNavigation)
    }
}