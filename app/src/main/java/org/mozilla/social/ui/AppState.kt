package org.mozilla.social.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import org.mozilla.social.navigation.TopLevelDestination

@Composable
fun rememberAppState(
    navController: NavController,
) : AppState = remember(navController) {
    AppState(navController)
}

class AppState(
    private val navController: NavController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            "feed" -> TopLevelDestination.FEED
            "search" -> TopLevelDestination.SEARCH
            "settings" -> TopLevelDestination.SETTINGS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    fun navigateToTopLevelDestination(
        destination: TopLevelDestination,
    ) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (destination) {
            TopLevelDestination.FEED -> navController.navigate("feed", navOptions)
            TopLevelDestination.SEARCH -> navController.navigate("search", navOptions)
            TopLevelDestination.SETTINGS -> navController.navigate("settings", navOptions)
        }
    }

    companion object {
        val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()
    }
}