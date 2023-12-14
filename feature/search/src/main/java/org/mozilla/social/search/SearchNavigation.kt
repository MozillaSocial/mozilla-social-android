package org.mozilla.social.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination


fun NavGraphBuilder.searchScreen() {
    composable(route = NavigationDestination.Search.route) {
        SearchScreen()
    }
}
