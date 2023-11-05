package org.mozilla.social.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.account.edit.editAccountScreen
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.followers.followersScreen
import org.mozilla.social.feature.followers.followingScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportFlow
import org.mozilla.social.feature.settings.settingsFlow
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.ui.AppState

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
        loginScreen()
        settingsScreen()
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

fun NavGraphBuilder.bottomTabScreen(appState: AppState) {
    composable(
        route = NavigationDestination.Tabs.route,
    ) {
        appState.tabbedNavController = rememberNavController()
        val currentDestination = appState.currentNavigationDestination.collectAsState().value!!

        Scaffold(
            modifier = Modifier,
            snackbarHost = {
                MoSoSnackbarHost(appState.snackbarHostState) { snackbarData, snackbarType ->
                    MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
                }
            },
            bottomBar = {
                BottomBar(
                    currentDestination = currentDestination,
                )
            },
            content = {
                // This is in a state flow because the value needs to be set
                val tabbedNavController = appState.tabbedNavControllerFlow.collectAsState().value
                if (tabbedNavController != null) {
                    BottomTabNavHost(
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                        tabbedNavController = tabbedNavController,
                    )
                }
            }
        )
    }
}

@Composable
private fun BottomBar(
    currentDestination: BottomBarNavigationDestination,
    navigateTo: NavigateTo = koinInject(),
) {
    Column {
        MoSoDivider()
        MoSoBottomNavigationBar(
            currentDestination = currentDestination,
            bottomBarTabs = BottomBarTabs.values().map { it.bottomBarTab },
            navigateTo = {
                when (it) {
                    is Destination.BottomBar -> {
                        navigateTo(it.bottomBarNavigationDestination)
                    }

                    is Destination.Main -> {
                        navigateTo(it.navigationDestination)
                    }
                }
            },
        )
    }
}