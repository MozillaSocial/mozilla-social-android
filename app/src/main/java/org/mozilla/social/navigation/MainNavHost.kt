package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.edit.editAccountScreen
import org.mozilla.social.feature.auth.authFlow
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.followers.followersScreen
import org.mozilla.social.feature.followers.followingScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportFlow
import org.mozilla.social.feature.settings.settingsFlow
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.ui.AppState
import org.mozilla.social.ui.SplashScreen
import org.mozilla.social.ui.bottombar.Routes
import org.mozilla.social.ui.bottombar.bottomTabScreen

@Composable
fun MainNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = appState.mainNavController,
        startDestination = Routes.SPLASH,
        enterTransition = { mosoFadeIn() },
        exitTransition = { mosoFadeOut() },
        popEnterTransition = { mosoFadeIn() },
        popExitTransition = { mosoFadeOut() },
    ) {
        splashScreen()
        authFlow()
        accountScreen()
        followersScreen()
        followingScreen()
        newPostScreen()
        threadScreen()
        reportFlow(navController = appState.mainNavController)
        hashTagScreen()
        bottomTabScreen(appState)
        editAccountScreen()
        settingsFlow()
    }
}

fun NavGraphBuilder.splashScreen() {
    composable(route = Routes.SPLASH) {
        SplashScreen()
    }
}
