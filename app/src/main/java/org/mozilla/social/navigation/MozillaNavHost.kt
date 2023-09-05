package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.follows.accountFollowersScreen
import org.mozilla.social.feature.account.follows.accountFollowingScreen
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.authScreen
import org.mozilla.social.feature.report.reportScreen
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.navigateToThread
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.feed.FEED_ROUTE
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.navigateToNewPost
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
        feedScreen(
            onReplyClicked = appState::navigateToNewPost,
            onPostClicked = appState::navigateToThread,
            onReportClicked = appState::navigateToReport,
        )
        searchScreen()
        settingsScreen(onLogout = appState::onLogout)
        accountScreen(
            userFollowing = appState::navigateToAccountFollowing,
            userFollowers = appState::navigateToAccountFollowers,
            onLogout = appState::onLogout,
        )
        accountFollowingScreen()
        accountFollowersScreen()
        newPostScreen(
            onStatusPosted = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
        threadScreen(
            onReplyClicked = appState::navigateToNewPost,
            onPostClicked = appState::navigateToThread,
            onCloseClicked = { appState.popBackStack() },
            onReportClicked = appState::navigateToReport,
        )
        reportScreen(
            onReported = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
    }
}

const val MAIN_ROUTE = "main"