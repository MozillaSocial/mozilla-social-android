package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.follows.accountFollowersScreen
import org.mozilla.social.feature.account.follows.accountFollowingScreen
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportScreen
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.feed.FEED_ROUTE
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.search.searchScreen
import org.mozilla.social.ui.AppState

@Composable
fun MozillaNavHost(appState: AppState) {
    NavHost(navController = appState.navController, startDestination = AUTH_ROUTE) {
        loginScreen(navigateToLoggedInGraph = appState::navigateToLoggedInGraph)
        mainGraph(appState = appState)

    }
}

private fun NavGraphBuilder.mainGraph(appState: AppState) {
    navigation(startDestination = FEED_ROUTE, route = MAIN_ROUTE) {
        val postCardNavigation = object: PostCardNavigation {
            override fun onReplyClicked(statusId: String) = appState.navigateToNewPost(statusId)
            override fun onPostClicked(statusId: String) = appState.navigateToThread(statusId)
            override fun onReportClicked(accountId: String, statusId: String) =
                appState.navigateToReport(accountId, statusId)
            override fun onAccountClicked(accountId: String) = appState.navigateToAccount(accountId)
        }

        feedScreen(
            postCardNavigation = postCardNavigation,
        )
        searchScreen()
        settingsScreen(onLogout = appState::navigateToLoginScreen)
        accountScreen(
            onFollowingClicked = appState::navigateToAccountFollowing,
            onFollowersClicked = appState::navigateToAccountFollowers,
            onLoggedOut = appState::navigateToLoginScreen,
        )
        accountFollowingScreen()
        accountFollowersScreen()
        newPostScreen(
            onStatusPosted = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
        threadScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = postCardNavigation,
        )
        reportScreen(
            onReported = { appState.popBackStack() },
            onCloseClicked = { appState.popBackStack() },
        )
        hashTagScreen(
            postCardNavigation = postCardNavigation,
        )
    }
}

const val MAIN_ROUTE = "main"