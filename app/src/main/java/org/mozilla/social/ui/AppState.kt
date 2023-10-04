package org.mozilla.social.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.feature.account.navigateToAccount
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.navigateToLoginScreen
import org.mozilla.social.feature.followers.navigateToFollowers
import org.mozilla.social.feature.followers.navigateToFollowing
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
 * Class to encapsulate high-level app state
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
    val currentDestination: StateFlow<NavDestination?> =
        navController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            navDestinations.find { it.route == backStackEntry.destination.route }
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialTopLevelDestination
        )

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

    fun navigateToAccountFollowing(accountId: String) {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToFollowing(accountId)
    }

    fun navigateToAccountFollowers(accountId: String) {
        coroutineScope.launch { navigationDrawerState.close() }
        navController.navigateToFollowers(accountId)
    }

    fun navigateToNewPost(replyStatusId: String? = null) {
        navController.navigateToNewPost(replyStatusId = replyStatusId)
    }

    fun navigateToThread(statusId: String) {
        navController.navigateToThread(threadStatusId = statusId)
    }

    fun navigateToReport(
        accountId: String,
        statusId: String? = null
    ) {
        navController.navigateToReport(reportAccountId = accountId, reportStatusId = statusId)
    }

    fun navigateToHashTag(hashTag: String) {
        navController.navigateToHashTag(hashTagValue = hashTag)
    }

    fun navigateToTopLevelDestination(destination: NavDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(destination.route, navOptions)
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