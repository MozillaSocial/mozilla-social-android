package org.mozilla.social.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(NavigationDestination.Search.route, navOptions)
}

fun NavGraphBuilder.searchScreen() {
    composable(route = NavigationDestination.Search.route) {
        SearchScreen()
    }
}