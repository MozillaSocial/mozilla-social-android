package org.mozilla.social.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.myAccountScreen
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.followers.followersScreen
import org.mozilla.social.feature.followers.followingScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportScreen
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.search.searchScreen
import org.mozilla.social.ui.AppState

@Composable
fun MozillaNavHost(appState: AppState) {
    NavHost(navController = appState.navController, startDestination = Routes.SPLASH) {
        splashScreen(
            navigateToLogin = appState::navigateToLoginScreen,
            navigateToLoggedInGraph = appState::navigateToLoggedInGraph,
        )
        loginScreen(navigateToLoggedInGraph = appState::navigateToLoggedInGraph)
        mainGraph(
            appState = appState,
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
) {
    navigation(startDestination = NavigationDestination.Feed.route, route = Routes.MAIN) {
        feedScreen(
            postCardNavigation = appState.postCardNavigation,
        )
        searchScreen()
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
            onStatusPosted = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
        threadScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = appState.postCardNavigation,
        )
        reportScreen(
            onReported = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
        hashTagScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = appState.postCardNavigation,
        )

        composable(route = NavigationDestination.Bookmarks.route) { Text(text = "bookmarks") }
    }
}
