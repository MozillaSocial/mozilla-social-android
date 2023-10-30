package org.mozilla.social.feed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination


fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(NavigationDestination.Feed.route, navOptions)
}

fun NavGraphBuilder.feedScreen(
) {
    composable(route = NavigationDestination.Feed.route) {
        FeedScreen()
    }
}