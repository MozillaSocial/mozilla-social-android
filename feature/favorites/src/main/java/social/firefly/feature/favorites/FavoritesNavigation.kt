package social.firefly.feature.favorites

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.favoritesScreen() {
    composable(route = NavigationDestination.Favorites.route) {
        FavoritesScreen()
    }
}
