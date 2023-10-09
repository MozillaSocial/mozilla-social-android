package org.mozilla.social.feed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.PostCardNavigation

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(NavigationDestination.Feed.route, navOptions)
}

fun NavGraphBuilder.feedScreen(
    postCardNavigation: PostCardNavigation,
) {
    composable(route = NavigationDestination.Feed.route) {
        FeedScreen(postCardNavigation)
    }
}