package org.mozilla.social.feature.favorites

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.favoritesScreen() {
    composable(route = NavigationDestination.Favorites.route) {
        FavoritesScreen()
    }
}
