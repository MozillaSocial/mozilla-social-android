package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.authScreen
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feed.FEED_ROUTE
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.search.searchScreen
import org.mozilla.social.ui.AppState

@Composable
fun MozillaNavHost(appState: AppState) {
    NavHost(navController = appState.navController, startDestination = AUTH_ROUTE) {
        authScreen(onAuthenticated = appState::navigateToMainGraph)
        mainGraph(appState = appState)
    }
}

private fun NavGraphBuilder.mainGraph(appState: AppState) {
    navigation(startDestination = FEED_ROUTE, route = MAIN_ROUTE) {

        feedScreen()
        searchScreen()
        settingsScreen(onLogout = appState::onLogout)
        newPostScreen(
            onStatusPosted = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
    }
}

const val MAIN_ROUTE = "main"