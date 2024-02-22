package social.firefly.ui

import android.content.Context
import android.util.Log
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
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.AuthNavigationDestination.ChooseServer.navigateToChooseServerScreen
import social.firefly.core.navigation.AuthNavigationDestination.Login.navigateToLoginScreen
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.NavigationDestination.Auth.navigateToAuthFlow
import social.firefly.core.navigation.NavigationDestination.EditAccount.navigateToEditAccount
import social.firefly.core.navigation.NavigationDestination.Favorites.navigateToFavorites
import social.firefly.core.navigation.NavigationDestination.Search.navigateToSearch
import social.firefly.core.navigation.NavigationDestination.Settings.navigateToSettings
import social.firefly.core.navigation.NavigationDestination.Tabs.navigateToTabs
import social.firefly.core.navigation.NavigationEventFlow
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.SettingsNavigationDestination.AboutSettings.navigateToAboutSettings
import social.firefly.core.navigation.SettingsNavigationDestination.AccountSettings.navigateToAccountSettings
import social.firefly.core.navigation.SettingsNavigationDestination.BlockedUsersSettings.navigateToBlockedUsers
import social.firefly.core.navigation.SettingsNavigationDestination.ContentPreferencesSettings.navigateToContentPreferencesSettings
import social.firefly.core.navigation.SettingsNavigationDestination.DeveloperOptions.navigateToDeveloperOptions
import social.firefly.core.navigation.SettingsNavigationDestination.MainSettings.navigateToMainSettings
import social.firefly.core.navigation.SettingsNavigationDestination.MutedUsersSettings.navigateToMutedUsers
import social.firefly.core.navigation.SettingsNavigationDestination.OpenSourceLicensesSettings.navigateToOpenSourceSettings
import social.firefly.core.navigation.SettingsNavigationDestination.PrivacySettings.navigateToPrivacySettings
import social.firefly.core.ui.common.snackbar.FfSnackbarHostState
import social.firefly.core.ui.common.snackbar.SnackbarType
import timber.log.Timber

@Composable
fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: FfSnackbarHostState = remember { FfSnackbarHostState() },
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
    initialBottomBarDestination: BottomBarNavigationDestination = BottomBarNavigationDestination.Feed,
    val mainNavController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: FfSnackbarHostState,
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
                Timber.d("NAVIGATION consuming event $it")
                when (it) {
                    is Event.NavigateToDestination -> {
                        navigate(it.destination)
                    }

                    is Event.NavigateToBottomBarDestination -> {
                        navigateToBottomBarDestination(it.destination)
                    }

                    Event.PopBackStack -> {
                        popBackStack()
                    }

                    is Event.OpenLink -> {
                        openLink(it.url)
                    }

                    is Event.ShowSnackbar -> {
                        showSnackbar(it.text, it.isError)
                    }

                    is Event.NavigateToSettingsDestination -> {
                        navigateToSettingsDestination(it.destination)
                    }

                    is Event.NavigateToLoginDestination -> {
                        navigateToAuthDestination(it.destination)
                    }
                }
            }
        }
    }

    private fun showSnackbar(
        text: StringFactory,
        error: Boolean,
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                snackbarType = if (error) SnackbarType.ERROR else SnackbarType.SUCCESS,
                message = text.build(context),
            )
        }
    }

    private fun openLink(url: String) {
        var uri = url.toUri()

        try {
            if (uri.scheme.isNullOrBlank()) {
                uri = uri.buildUpon().scheme(HTTPS_SCHEME).build()
            }

            CustomTabsIntent.Builder()
                .build()
                .launchUrl(
                    context,
                    uri,
                )
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentNavigationDestination: StateFlow<NavigationDestination?> =
        mainNavController.currentBackStackEntryFlow.mapLatest { backStackEntry ->
            NavigationDestination::class.sealedSubclasses.firstOrNull {
                it.objectInstance?.route == backStackEntry.destination.route
            }?.objectInstance
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentBottomBarNavigationDestination: StateFlow<BottomBarNavigationDestination?> =
        tabbedNavControllerFlow.flatMapLatest { navHostController ->
            navHostController?.currentBackStackEntryFlow?.mapLatest { backStackEntry ->
                BottomBarNavigationDestination::class.sealedSubclasses.firstOrNull {
                    it.objectInstance?.route == backStackEntry.destination.route
                }?.objectInstance
            } ?: error("no matching nav destination")
        }.stateIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialBottomBarDestination,
        )

    private fun clearBackstack() {
        while (mainNavController.currentBackStack.value.isNotEmpty()) {
            mainNavController.popBackStack()
        }
    }

    private fun popBackStack() {
        if (!mainNavController.navigateUp()) tabbedNavController?.navigateUp()
    }

    @Suppress("CyclomaticComplexMethod")
    private fun navigate(navDestination: NavigationDestination) {
        Timber.d("NAVIGATION consuming $navDestination")
        with(navDestination) {
            when (this) {
                is NavigationDestination.Account -> {
                    mainNavController.navigateToAccount()
                }

                NavigationDestination.Auth -> {
                    clearBackstack()
                    mainNavController.navigateToAuthFlow()
                }

                NavigationDestination.EditAccount -> {
                    mainNavController.navigateToEditAccount()
                }

                NavigationDestination.Favorites -> {
                    mainNavController.navigateToFavorites()
                }

                is NavigationDestination.Followers -> {
                    mainNavController.navigateToFollowing()
                }

                is NavigationDestination.HashTag -> {
                    mainNavController.navigateToHashTag()
                }

                is NavigationDestination.Media -> {
                    mainNavController.navigateToMedia()
                }

                is NavigationDestination.NewPost -> {
                    mainNavController.navigateToNewPost()
                }

                is NavigationDestination.Report -> {
                    mainNavController.navigateToReport()
                }

                NavigationDestination.Search -> {
                    mainNavController.navigateToSearch()
                }

                NavigationDestination.Settings -> {
                    mainNavController.navigateToSettings()
                }

                NavigationDestination.Tabs -> {
                    clearBackstack()
                    mainNavController.navigateToTabs()
                }

                is NavigationDestination.Thread -> {
                    mainNavController.navigateToThread()
                }
            }
        }
    }

    private fun navigateToSettingsDestination(destination: SettingsNavigationDestination) {
        when (destination) {
            SettingsNavigationDestination.AboutSettings -> {
                mainNavController.navigateToAboutSettings()
            }

            SettingsNavigationDestination.AccountSettings -> {
                mainNavController.navigateToAccountSettings()
            }

            SettingsNavigationDestination.PrivacySettings -> {
                mainNavController.navigateToPrivacySettings()
            }

            SettingsNavigationDestination.MainSettings -> {
                mainNavController.navigateToMainSettings()
            }

            SettingsNavigationDestination.BlockedUsersSettings -> {
                mainNavController.navigateToBlockedUsers()
            }

            SettingsNavigationDestination.ContentPreferencesSettings -> {
                mainNavController.navigateToContentPreferencesSettings()
            }

            SettingsNavigationDestination.MutedUsersSettings -> {
                mainNavController.navigateToMutedUsers()
            }

            SettingsNavigationDestination.OpenSourceLicensesSettings -> {
                mainNavController.navigateToOpenSourceSettings()
            }

            SettingsNavigationDestination.DeveloperOptions -> {
                mainNavController.navigateToDeveloperOptions()
            }
        }
    }

    private fun navigateToAuthDestination(destination: AuthNavigationDestination) {
        when (destination) {
            AuthNavigationDestination.Login -> {
                mainNavController.navigateToLoginScreen()
            }

            AuthNavigationDestination.ChooseServer -> {
                mainNavController.navigateToChooseServerScreen()
            }
        }
    }

    private fun navigateToBottomBarDestination(destination: BottomBarNavigationDestination) {
        Timber.d("NAVIGATION navigate to bottom bar destination: $destination")

        // If navigating to the feed, just pop up to the feed.  Don't start a new instance
        // of it.  If a new instance is started, we don't retain scroll position!
        if (destination == BottomBarNavigationDestination.Feed) {
            tabbedNavController?.popBackStack(
                BottomBarNavigationDestination.Feed.route,
                false,
            )
        }
        val navOptions =
            navOptions {
                popUpTo(BottomBarNavigationDestination.Feed.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

        tabbedNavController?.navigate(destination.route, navOptions)
    }

    companion object {
        private const val HTTPS_SCHEME = "https"
    }
}
