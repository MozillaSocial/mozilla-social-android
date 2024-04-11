package social.firefly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import social.firefly.common.utils.ffFadeIn
import social.firefly.common.utils.ffFadeOut
import social.firefly.feature.account.accountScreen
import social.firefly.feature.account.edit.editAccountScreen
import social.firefly.feature.auth.authFlow
import social.firefly.feature.favorites.favoritesScreen
import social.firefly.feature.followers.followersScreen
import social.firefly.feature.hashtag.hashTagScreen
import social.firefly.feature.media.mediaScreen
import social.firefly.feature.report.reportFlow
import social.firefly.feature.settings.settingsFlow
import social.firefly.feature.thread.threadScreen
import social.firefly.post.newPostScreen
import social.firefly.search.searchScreen
import social.firefly.ui.AppState
import social.firefly.ui.bottombar.Routes
import social.firefly.ui.bottombar.bottomTabScreen

@Composable
fun MainNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = appState.mainNavController,
        startDestination = Routes.SPLASH,
        enterTransition = { ffFadeIn() },
        exitTransition = { ffFadeOut() },
        popEnterTransition = { ffFadeIn() },
        popExitTransition = { ffFadeOut() },
    ) {
        splashScreen()
        authFlow()
        accountScreen()
        followersScreen()
        newPostScreen()
        threadScreen()
        reportFlow(navController = appState.mainNavController)
        hashTagScreen()
        bottomTabScreen(appState)
        editAccountScreen()
        settingsFlow()
        favoritesScreen()
        searchScreen()
        mediaScreen()
    }
}

fun NavGraphBuilder.splashScreen() {
    composable(route = Routes.SPLASH) {}
}
