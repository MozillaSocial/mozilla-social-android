package org.mozilla.social.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch
import org.mozilla.social.core.designsystem.component.MoSoAppBar
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.MoSoModalDrawerSheet
import org.mozilla.social.core.designsystem.component.MoSoNavigationDrawerItem
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.feature.account.navigateToAccount
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.navigateToAuth
import org.mozilla.social.feature.settings.navigateToSettings
import org.mozilla.social.navigation.Account
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
    navigationDrawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    bottomSheetVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
): AppState = remember(navController) {
    AppState(
        navController = navController,
        topAppBarScrollBehavior = topAppBarScrollBehavior,
        navigationDrawerState = navigationDrawerState,
        coroutineScope = coroutineScope,
        bottomSheetVisible = bottomSheetVisible,
        snackbarHostState = snackbarHostState,
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
    val topAppBarScrollBehavior: TopAppBarScrollBehavior,
    val navigationDrawerState: DrawerState,
    val coroutineScope: CoroutineScope,
    val bottomSheetVisible: MutableState<Boolean>,
    val snackbarHostState: SnackbarHostState,
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

            NewPost, Search, Account, null -> {}
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topBar() {
        val currentDestination = currentDestination.collectAsState().value

        val titleText = when (currentDestination) {
            Feed -> {
                "Mozilla Social"
            }

            Search -> {
                "Search"
            }

            Account -> {
                "Account"
            }

            else -> {
                null
            }
        }

        when (currentDestination) {
            Feed, Search, Account -> {
                MoSoAppBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    title = {
                        if (titleText != null) {
                            Text(
                                text = titleText,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    navigationIcon = {
                        when (currentDestination) {
                            Feed, Search, Account -> {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        navigationDrawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = MoSoIcons.Menu,
                                        contentDescription = "navigation menu",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }

                            else -> {}
                        }
                    },
                    actions = {
                        if (currentDestination == Feed) {
                            IconButton(onClick = {
                                bottomSheetVisible.value = !bottomSheetVisible.value
                            }) {
                                Icon(
                                    imageVector = MoSoIcons.Feed,
                                    contentDescription = "feed selection",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                )
            }

            else -> {

            }
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

    fun navigateToSettings() {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToSettings()
    }

    fun navigateToAccount() {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToAccount()
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

    @Composable
    fun navigationDrawerContent(onSettingsClicked: () -> Unit) {
        MoSoModalDrawerSheet {
            Text("mozilla.social", modifier = Modifier.padding(16.dp))

            Divider()
            // TODO
//                MoSoNavigationDrawerItem("Profile")
//                MoSoNavigationDrawerItem("Favorites")
//                MoSoNavigationDrawerItem("Bookmarks")
            MoSoNavigationDrawerItem("Settings", onClick = onSettingsClicked)
        }

    }

    @Composable
    fun bottomSheetContent() {
        Text(text = "feed options coming")
    }

    companion object {
        /**
         * All navigation destinations corresponding to nav bar tabs
         */
        val navBarDestinations: List<NavBarDestination> = listOf(Feed, Search, Account)

        /**
         * All navigation destinations in the graph
         */
        val navDestinations: List<NavDestination> = listOf(Feed, Search, Account, NewPost)
    }
}