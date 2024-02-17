package social.firefly.feed

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.feedScreen() {
    composable(route = BottomBarNavigationDestination.Feed.route) {
        FeedScreen()
    }
}
