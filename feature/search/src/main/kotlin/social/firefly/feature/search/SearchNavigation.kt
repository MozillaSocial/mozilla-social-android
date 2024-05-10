package social.firefly.feature.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.NavigationDestination


fun NavGraphBuilder.searchScreen() {
    composable(route = NavigationDestination.Search.route) {
        SearchScreen()
    }
}
