package org.mozilla.social.navigation

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.SnackbarType
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.myAccountScreen
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.discover.discoverScreen
import org.mozilla.social.feature.followers.followersScreen
import org.mozilla.social.feature.followers.followingScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportFlow
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.search.searchScreen
import org.mozilla.social.ui.AppState

@Composable
fun MozillaNavHost(appState: AppState, context: Context) {
    NavHost(navController = appState.navController, startDestination = Routes.SPLASH) {
        splashScreen(
            navigateToLogin = appState::navigateToLoginScreen,
            navigateToLoggedInGraph = appState::navigateToLoggedInGraph,
        )
        loginScreen(navigateToLoggedInGraph = appState::navigateToLoggedInGraph)
        mainGraph(
            appState = appState,
            context = context,
        )
    }
}

fun NavGraphBuilder.splashScreen(
    navigateToLogin: () -> Unit,
    navigateToLoggedInGraph: () -> Unit
) {
    composable(route = Routes.SPLASH) {
        SplashScreen(
            navigateToLogin = navigateToLogin,
            navigateToLoggedInGraph = navigateToLoggedInGraph
        )
    }
}

private fun NavGraphBuilder.mainGraph(
    appState: AppState,
    context: Context,
) {
    navigation(startDestination = NavigationDestination.Feed.route, route = Routes.MAIN) {
        feedScreen(
            postCardNavigation = appState.postCardNavigation,
        )
        searchScreen()
        discoverScreen()
        settingsScreen(onLogout = appState::navigateToLoginScreen)
        accountScreen(
            accountNavigationCallbacks = appState.accountNavigation,
            postCardNavigation = appState.postCardNavigation,
        )
        myAccountScreen(
            accountNavigationCallbacks = appState.accountNavigation,
            postCardNavigation = appState.postCardNavigation,
        )
        followersScreen(followersNavigationCallbacks = appState.followersNavigation)
        followingScreen(followersNavigationCallbacks = appState.followersNavigation)
        newPostScreen(
            onStatusPosted = {
                appState.popBackStack()
                GlobalScope.launch {
                    appState.snackbarHostState.showSnackbar(
                        snackbarType = SnackbarType.SUCCESS,
                        message = context.getString(R.string.your_post_was_published)
                    )
                }
            },
            onCloseClicked = { appState.popBackStack() },
        )
        threadScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = appState.postCardNavigation,
        )
        reportFlow(
            navController = appState.navController,
        )
        hashTagScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = appState.postCardNavigation,
        )

        composable(route = NavigationDestination.Bookmarks.route) { Text(text = "bookmarks") }
    }
}
