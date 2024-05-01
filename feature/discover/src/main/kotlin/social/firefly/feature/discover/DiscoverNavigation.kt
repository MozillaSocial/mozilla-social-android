package social.firefly.feature.discover

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.discoverScreen() {
    composable(
        route = BottomBarNavigationDestination.Discover.route,
    ) {
        DiscoverScreen()
    }
}
