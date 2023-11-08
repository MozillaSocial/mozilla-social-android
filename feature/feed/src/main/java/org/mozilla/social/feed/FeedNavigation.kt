package org.mozilla.social.feed

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.feedScreen(
) {
    composable(route = BottomBarNavigationDestination.Feed.route) {
        FeedScreen()
    }
}