package org.mozilla.social.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.core.designsystem.component.MoSoAppBar
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.navigateToAuth
import org.mozilla.social.navigation.Feed
import org.mozilla.social.navigation.MAIN_ROUTE
import org.mozilla.social.navigation.NewPost
import org.mozilla.social.navigation.Search
import org.mozilla.social.navigation.Settings
import org.mozilla.social.post.navigateToNewPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
): AppState = remember(navController) {
    AppState(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        topAppBarScrollBehavior = topAppBarScrollBehavior,
    )
}

/**
 * Class to encapsulate high-level app state, including UI elements in the top-level scaffold and
 * navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
class AppState(
    initialTopLevelDestination: NavDestination = Feed,
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val topAppBarScrollBehavior: TopAppBarScrollBehavior,
    coroutineScope: CoroutineScope,
) {

    private val currentDestination: StateFlow<NavDestination?> =
        navController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            navDestinations.find { it.route == backStackEntry.destination.route }
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialTopLevelDestination
        )

    @Composable
    fun floatingActionButton() {
        when (currentDestination.collectAsState().value) {
            Feed -> {
                FloatingActionButton(onClick = { navController.navigateToNewPost() }) {
                    Icon(
                        Icons.Rounded.Add,
                        "new post"
                    )
                }
            }

            NewPost, Search, Settings, null -> {}
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topBar() {
        if (currentDestination.collectAsState().value == Feed) {
            MoSoAppBar(scrollBehavior = topAppBarScrollBehavior)
        }
    }

    @Composable
    fun bottomBar() {
        val currentDestination =
            currentDestination.collectAsState().value as? NavBarDestination ?: return

        MoSoBottomNavigationBar(
            currentDestination = currentDestination,
            navBarDestinations = navBarDestinations,
            navigateTo = { navigateToTopLevelDestination(it) }
        )
    }

    private fun clearBackstack() {
        while (navController.currentBackStack.value.isNotEmpty()) {
            navController.popBackStack()
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    private fun navigateToAuth() {
        navController.navigateToAuth()
    }

    fun navigateToMainGraph() {
        navController.navigate(
            MAIN_ROUTE,
            navOptions = NavOptions.Builder()
                .setPopUpTo(AUTH_ROUTE, true)
                .build()
        )
    }

    private fun navigateToTopLevelDestination(destination: NavDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(destination.route, navOptions)
    }

    fun onLogout() {
        clearBackstack()
        navigateToAuth()
    }

    companion object {
        /**
         * All navigation destinations corresponding to nav bar tabs
         */
        val navBarDestinations: List<NavBarDestination> = listOf(Feed, Search, Settings)

        /**
         * All navigation destinations in the graph
         */
        val navDestinations: List<NavDestination> = listOf(Feed, Search, Settings, NewPost)
    }
}