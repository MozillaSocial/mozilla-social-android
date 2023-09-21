package org.mozilla.social.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.MoSoAppBar
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoModalDrawerSheet
import org.mozilla.social.core.designsystem.component.MoSoNavigationDrawerItem
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.icon.mozillaLogo
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.feature.account.follows.navigateToAccountFollowers
import org.mozilla.social.feature.account.follows.navigateToAccountFollowing
import org.mozilla.social.feature.account.navigateToAccount
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.navigateToLoginScreen
import org.mozilla.social.feature.hashtag.navigateToHashTag
import org.mozilla.social.feature.report.navigateToReport
import org.mozilla.social.feature.settings.navigateToSettings
import org.mozilla.social.feature.thread.navigateToThread
import org.mozilla.social.navigation.Account
import org.mozilla.social.navigation.Bookmarks
import org.mozilla.social.navigation.Discover
import org.mozilla.social.navigation.Feed
import org.mozilla.social.navigation.NewPost
import org.mozilla.social.navigation.Routes
import org.mozilla.social.post.navigateToNewPost
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
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
    val navController: NavHostController, // Don't access this other than for initializing the nav host
    val topAppBarScrollBehavior: TopAppBarScrollBehavior,
    val navigationDrawerState: DrawerState,
    val coroutineScope: CoroutineScope,
    val bottomSheetVisible: MutableState<Boolean>,
    val snackbarHostState: SnackbarHostState,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentDestination: StateFlow<NavDestination?> =
        navController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            navDestinations.find { it.route == backStackEntry.destination.route }
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialTopLevelDestination
        )

    @Composable
    fun FloatingActionButton() {
        when (currentDestination.collectAsState().value) {
            Feed -> {
                FloatingActionButton(onClick = { navController.navigateToNewPost() }) {
                    Icon(
                        MoSoIcons.Add,
                        stringResource(id = R.string.feed_fab_content_description)
                    )
                }
            }

            NewPost, Bookmarks, Account, null -> {}
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        val currentDestination = currentDestination.collectAsState().value

        val titleText = when (currentDestination) {
            Feed -> {
                stringResource(id = R.string.top_bar_title_feed)
            }

            Bookmarks -> {
                stringResource(id = R.string.top_bar_title_search)
            }

            Account -> {
                stringResource(id = R.string.top_bar_title_account)
            }

            else -> {
                null
            }
        }

        when (currentDestination) {
            Feed, Discover, Bookmarks, Account -> {
                Column {
                    MoSoAppBar(
                        scrollBehavior = topAppBarScrollBehavior,
                        title = {
                            Image(
                                painter = mozillaLogo(),
                                contentDescription = "mozilla logo"
                            )
                        },
                        navigationIcon = {
                            when (currentDestination) {
                                Feed, Discover, Account, Bookmarks -> {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            navigationDrawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            painter = MoSoIcons.list(),
                                            modifier = Modifier.size(24.dp),
                                            contentDescription = stringResource(id = R.string.navigation_menu_content_description),
                                        )
                                    }
                                }

                                else -> {}
                            }
                        },
                        actions = {
//                            if (currentDestination == Feed) {
//                                IconButton(onClick = {
//                                    bottomSheetVisible.value = !bottomSheetVisible.value
//                                }) {
//                                    Icon(
//                                        imageVector = MoSoIcons.Feed,
//                                        contentDescription = stringResource(id = R.string.feed_selection_content_description),
//                                        tint = MaterialTheme.colorScheme.onPrimary,
//                                    )
//                                }
//                            }
                        }
                    )

                    MoSoDivider(
                        modifier = Modifier
                            .height(1.dp)
                            .background(MoSoTheme.colors.borderPrimary)
                    )
                }
            }

            else -> {

            }
        }
    }


    @Composable
    fun BottomBar() {
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

    /**
     * Navigate to the login screen when the user is logged out
     */
    fun navigateToLoginScreen() {
        coroutineScope.launch { navigationDrawerState.close() }
        Timber.d("navigate to login screen")
        clearBackstack()
        navController.navigateToLoginScreen()
    }

    /**
     * Navigate to the main graph once the user is logged in
     */
    fun navigateToLoggedInGraph() {
        Timber.d("navigate to login graph")

        navController.navigate(
            Routes.MAIN,
            navOptions = NavOptions.Builder()
                .setPopUpTo(AUTH_ROUTE, true)
                .build()
        )
    }

    fun navigateToSplashScreen() {
        navController.navigate(Routes.SPLASH)
    }

    fun navigateToSettings() {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToSettings()
    }

    fun navigateToAccount(
        accountId: String? = null,
    ) {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToAccount(
            accountId = accountId,
        )
    }

    fun navigateToAccountFollowing() {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToAccountFollowing()
    }

    fun navigateToAccountFollowers() {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToAccountFollowers()
    }

    fun navigateToNewPost(replyStatusId: String) {
        navController.navigateToNewPost(replyStatusId = replyStatusId)
    }

    fun navigateToThread(statusId: String) {
        navController.navigateToThread(threadStatusId = statusId)
    }

    fun navigateToReport(accountId: String, statusId: String?) {
        navController.navigateToReport(reportAccountId = accountId, reportStatusId = statusId)
    }

    fun navigateToHashTag(hashTag: String) {
        navController.navigateToHashTag(hashTagValue = hashTag)
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

    @Composable
    fun BottomSheetContent() {
        Text(text = "feed options coming")
    }

    companion object {
        /**
         * All navigation destinations corresponding to nav bar tabs
         */
        val navBarDestinations: List<NavBarDestination> =
            listOf(Feed, Discover, Bookmarks, Account)

        /**
         * All navigation destinations in the graph
         */
        val navDestinations: List<NavDestination> = navBarDestinations + listOf(NewPost)
    }
}