package org.mozilla.social.ui

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHostState
import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.feature.account.AccountNavigationCallbacks
import org.mozilla.social.feature.account.navigateToAccount
import org.mozilla.social.feature.auth.navigateToLoginScreen
import org.mozilla.social.feature.followers.FollowersNavigationCallbacks
import org.mozilla.social.feature.followers.navigateToFollowers
import org.mozilla.social.feature.followers.navigateToFollowing
import org.mozilla.social.feature.hashtag.navigateToHashTag
import org.mozilla.social.feature.report.navigateToReport
import org.mozilla.social.feature.settings.navigateToSettings
import org.mozilla.social.feature.thread.navigateToThread
import org.mozilla.social.feed.navigateToFeed
import org.mozilla.social.core.navigation.NavigationEventFlow
import org.mozilla.social.navigation.Routes
import org.mozilla.social.post.navigateToNewPost
import timber.log.Timber

@Composable

fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    tabbedNavController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: MoSoSnackbarHostState = remember { MoSoSnackbarHostState() },
    navigationEventFlow: NavigationEventFlow = koinInject(),
): AppState {
    val context = LocalContext.current

    return remember(mainNavController) {
        AppState(
            mainNavController = mainNavController,
            tabbedNavController = tabbedNavController,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            context = context,
            navigationEventFlow = navigationEventFlow,
        )
    }
}

/**
 * Class to encapsulate high-level app state
 */
class AppState(
    initialTopLevelDestination: NavigationDestination = NavigationDestination.Feed,
    val mainNavController: NavHostController,
    val tabbedNavController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: MoSoSnackbarHostState,
    val context: Context,
    val navigationEventFlow: NavigationEventFlow,
) {
    init {
        coroutineScope.launch(Dispatchers.Main) {
            navigationEventFlow().collectLatest {
                println("navigate event consumed: $it")
                navigate(it)
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNavigationDestination: StateFlow<NavigationDestination?> =
        tabbedNavController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            NavigationDestination::class.sealedSubclasses.firstOrNull {
                it.objectInstance?.route == backStackEntry.destination.route
            }?.objectInstance
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialTopLevelDestination
        )

    val postCardNavigation = object : PostCardNavigation {
        override fun onReplyClicked(statusId: String) = navigateToNewPost(statusId)
        override fun onPostClicked(statusId: String) = navigateToThread(statusId)
        override fun onReportClicked(
            accountId: String,
            accountHandle: String,
            statusId: String,
        ) = navigateToReport(
            accountId = accountId,
            accountHandle = accountHandle,
            statusId = statusId,
        )

        override fun onAccountClicked(accountId: String) = navigateToAccount(accountId)
        override fun onHashTagClicked(hashTag: String) = navigateToHashTag(hashTag)
        override fun onLinkClicked(url: String) {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(
                    context,
                    url.toUri(),
                )
        }
    }

    val accountNavigation = object : AccountNavigationCallbacks {
        override fun onFollowingClicked(accountId: String) =
            navigateToAccountFollowing(accountId)

        override fun onFollowersClicked(accountId: String) =
            navigateToAccountFollowers(accountId)

        override fun onCloseClicked() = popBackStack()
        override fun onReportClicked(
            accountId: String,
            accountHandle: String,
        ) = navigateToReport(
            accountId = accountId,
            accountHandle = accountHandle
        )

        override fun navigateToSettings() {
//            navigateToSettings()
        }
    }

    val followersNavigation = object : FollowersNavigationCallbacks {
        override fun onCloseClicked() = popBackStack()
        override fun onAccountClicked(accountId: String) = navigateToAccount(accountId)
    }

    private fun clearBackstack() {
        while (mainNavController.currentBackStack.value.isNotEmpty()) {
            mainNavController.popBackStack()
        }
    }

    fun popBackStack() {
        mainNavController.popBackStack()
    }

    fun navigate(navDestination: NavDestination) {
        with(navDestination) {
            println("nav consume $navDestination")
            when (this) {
                NavDestination.Login -> {
                    navigateToLoginScreen()
                }

                is NavDestination.NewPost -> {
                    navigateToNewPost(replyStatusId = replyId)
                }

                is NavDestination.Report -> {
                    navigateToReport(
                        accountId = accountId,
                        accountHandle = accountHandle,
                        statusId = statusId,
                    )
                }

                is NavDestination.Thread -> {
                    navigateToThread(statusId)
                }

                NavDestination.Feed -> {
                    navigateToLoggedInGraph()
                }

                is NavDestination.Following -> TODO()
            }
        }
    }

    /**
     * Navigate to the login screen when the user is logged out
     */
    fun navigateToLoginScreen() {
        Timber.d("navigate to login screen")
        clearBackstack()
        mainNavController.navigateToLoginScreen()
    }

    /**
     * Navigate to the main graph once the user is logged in
     */
    fun navigateToLoggedInGraph() {
        Timber.d("navigate to login graph")

        mainNavController.navigateToFeed()

//        navController.navigate(
//            Routes.MAIN,
//            navOptions = NavOptions.Builder()
//                .setPopUpTo(NavigationDestination.Auth.route, true)
//                .build()
//        )
    }

    fun navigateToSplashScreen() {
        mainNavController.navigate(Routes.SPLASH)
    }

    fun navigateToSettings() {
        mainNavController.navigateToSettings()
    }

    fun navigateToAccount(
        accountId: String,
    ) {
        mainNavController.navigateToAccount(
            accountId = accountId,
        )
    }

    fun navigateToAccountFollowing(accountId: String) {
        mainNavController.navigateToFollowing(accountId)
    }

    fun navigateToAccountFollowers(accountId: String) {
        mainNavController.navigateToFollowers(accountId)
    }

    fun navigateToNewPost(replyStatusId: String? = null) {
        mainNavController.navigateToNewPost(replyStatusId = replyStatusId)
    }

    fun navigateToThread(statusId: String) {
        mainNavController.navigateToThread(threadStatusId = statusId)
    }

    fun navigateToReport(
        accountId: String,
        accountHandle: String,
        statusId: String? = null
    ) {
        mainNavController.navigateToReport(
            reportAccountId = accountId,
            reportAccountHandle = accountHandle,
            reportStatusId = statusId,
        )
    }

    fun navigateToHashTag(hashTag: String) {
        mainNavController.navigateToHashTag(hashTagValue = hashTag)
    }

    /**
     * Used by bottom bar navigation
     */
    fun navigateToBottomBarDestination(destination: NavigationDestination) {
        // If navigating to the feed, just pop up to the feed.  Don't start a new instance
        // of it.  If a new instance is started, we don't retain scroll position!
        if (destination == NavigationDestination.Feed) {
            tabbedNavController.popBackStack(NavigationDestination.Feed.route, false)
            return
        }
        val navOptions = navOptions {
            popUpTo(NavigationDestination.Feed.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        tabbedNavController.navigate(destination.route, navOptions)
    }

    companion object {
        fun shouldShowTopBar(
            currentDestination: NavigationDestination?
        ): Boolean = when (currentDestination) {
            NavigationDestination.Feed,
            NavigationDestination.Discover,
            NavigationDestination.Bookmarks -> true

            else -> false
        }
    }
}