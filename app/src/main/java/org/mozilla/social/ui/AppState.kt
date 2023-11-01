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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHostState
import org.mozilla.social.core.designsystem.component.SnackbarType
import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.NavigationEventFlow
import org.mozilla.social.feature.account.edit.navigateToEditAccount
import org.mozilla.social.feature.account.navigateToAccount
import org.mozilla.social.feature.auth.navigateToLoginScreen
import org.mozilla.social.feature.followers.navigateToFollowers
import org.mozilla.social.feature.followers.navigateToFollowing
import org.mozilla.social.feature.hashtag.navigateToHashTag
import org.mozilla.social.feature.report.navigateToReport
import org.mozilla.social.feature.settings.navigateToSettings
import org.mozilla.social.feature.thread.navigateToThread
import org.mozilla.social.navigation.navigateToTabs
import org.mozilla.social.post.navigateToNewPost
import timber.log.Timber

@Composable
fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: MoSoSnackbarHostState = remember { MoSoSnackbarHostState() },
    navigationEventFlow: NavigationEventFlow = koinInject(),
): AppState {
    val context = LocalContext.current

    return remember(mainNavController) {
        AppState(
            mainNavController = mainNavController,
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
    val coroutineScope: CoroutineScope,
    val snackbarHostState: MoSoSnackbarHostState,
    val context: Context,
    val navigationEventFlow: NavigationEventFlow,
) {
    // The tabbedNavController is stored in a StateFlow so that we can make sure there's no
    // requests to navigate until the BottomTabNavHost has reached composition
    private val _tabbedNavControllerFlow = MutableStateFlow<NavHostController?>(null)
    val tabbedNavControllerFlow: StateFlow<NavHostController?> =
        _tabbedNavControllerFlow.asStateFlow()

    // Convenience var for getting/setting value in flow
    var tabbedNavController: NavHostController?
        get() = tabbedNavControllerFlow.value
        set(value) {
            _tabbedNavControllerFlow.value = value
        }

    init {
        coroutineScope.launch(Dispatchers.Main) {
            navigationEventFlow().collectLatest {
                Timber.d("consuming event $it")
                when (it) {
                    is Event.NavigateToDestination -> navigate(it.destination)
                    Event.PopBackStack -> popBackStack()
                    is Event.OpenLink -> openLink(it.url)
                    is Event.ShowSnackbar -> {
                        showSnackbar(it.text, it.isError)
                    }
                }
            }
        }
    }

    private fun showSnackbar(text: StringFactory, error: Boolean) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                snackbarType = if (error) SnackbarType.ERROR else SnackbarType.SUCCESS,
                message = text.build(context)
            )
        }
    }

    private fun openLink(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                context,
                url.toUri(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNavigationDestination: StateFlow<NavigationDestination?> =
        tabbedNavControllerFlow.flatMapLatest {
            it?.currentBackStackEntryFlow?.mapLatest { backStackEntry ->
                NavigationDestination::class.sealedSubclasses.firstOrNull {
                    it.objectInstance?.route == backStackEntry.destination.route
                }?.objectInstance
            } ?: error("no matching nav destination")
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialTopLevelDestination
        )

    private fun clearBackstack() {
        while (mainNavController.currentBackStack.value.isNotEmpty()) {
            mainNavController.popBackStack()
        }
    }

    private fun popBackStack() {
        if (!mainNavController.popBackStack()) tabbedNavController?.popBackStack()
    }

    private fun navigate(navDestination: NavDestination) {
        Timber.d("NAVIGATION consuming $navDestination")
        with(navDestination) {
            when (this) {
                NavDestination.Login -> {
                    clearBackstack()
                    mainNavController.navigateToLoginScreen()
                }

                is NavDestination.NewPost -> {
                    mainNavController.navigateToNewPost(replyStatusId = replyId)
                }

                is NavDestination.Report -> {
                    mainNavController.navigateToReport(
                        reportAccountId = accountId,
                        reportAccountHandle = accountHandle,
                        reportStatusId = statusId,
                    )
                }

                is NavDestination.Thread -> mainNavController.navigateToThread(threadStatusId = statusId)
                is NavDestination.Following -> mainNavController.navigateToFollowing(accountId)
                is NavDestination.Account -> mainNavController.navigateToAccount(accountId = accountId)
                is NavDestination.Followers -> mainNavController.navigateToFollowers(accountId)
                NavDestination.Settings -> mainNavController.navigateToSettings()
                NavDestination.Tabs -> {
                    clearBackstack()
                    mainNavController.navigateToTabs()
                }

                is NavDestination.Hashtag -> mainNavController.navigateToHashTag(hashTagValue = hashtag)
                NavDestination.EditAccount -> mainNavController.navigateToEditAccount()
            }
        }
    }

    /**
     * Used by bottom bar navigation
     */
    fun navigateToBottomBarDestination(destination: NavigationDestination) {
        Timber.d("NAVIGATION navigate to bottom bar destination: $destination")
        // If navigating to the feed, just pop up to the feed.  Don't start a new instance
        // of it.  If a new instance is started, we don't retain scroll position!
        if (destination == NavigationDestination.Feed) {
            tabbedNavController?.popBackStack(NavigationDestination.Feed.route, false)
        }
        val navOptions = navOptions {
            popUpTo(NavigationDestination.Feed.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        tabbedNavController?.navigate(destination.route, navOptions)
    }
}