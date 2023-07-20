package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.authScreen
import org.mozilla.social.feature.auth.navigateToAuth
import org.mozilla.social.feature.settings.SettingsRoute
import org.mozilla.social.feed.FEED_ROUTE
import org.mozilla.social.feed.feedScreen
import org.mozilla.social.post.navigateToNewPost
import org.mozilla.social.post.newPostScreen

@Composable
fun MozillaNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AUTH_ROUTE) {
        authScreen(onAuthenticated = {
            navController.navigate(
                MAIN_ROUTE,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(AUTH_ROUTE, true)
                    .build()
            )
        })
        mainGraph(navController)
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = FEED_ROUTE, route = MAIN_ROUTE) {

        feedScreen(
            onNewPostClicked = { navController.navigateToNewPost() },
        )
        composable("settings") {
            SettingsRoute(
                onLogout = {
                    while (navController.currentBackStack.value.isNotEmpty()) {
                        navController.popBackStack()
                    }
                    navController.navigateToAuth()
                })
        }
        newPostScreen(
            onStatusPosted = { navController.popBackStack() },
            onCloseClicked = { navController.popBackStack() },
        )
    }
}

const val MAIN_ROUTE = "main"