package org.mozilla.social.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mozilla.social.R
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.core.designsystem.component.MoSoFloatingActionButton
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.designsystem.component.SnackbarType
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.account.accountScreen
import org.mozilla.social.feature.auth.loginScreen
import org.mozilla.social.feature.followers.followersScreen
import org.mozilla.social.feature.followers.followingScreen
import org.mozilla.social.feature.hashtag.hashTagScreen
import org.mozilla.social.feature.report.reportFlow
import org.mozilla.social.feature.settings.settingsScreen
import org.mozilla.social.feature.thread.threadScreen
import org.mozilla.social.post.newPostScreen
import org.mozilla.social.search.searchScreen
import org.mozilla.social.ui.AppState

@Composable
fun MainNavHost(
    appState: AppState,
    context: Context,
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
        loginScreen(navigateToLoggedInGraph = appState::navigateToLoggedInGraph)

        searchScreen()
        settingsScreen()
        accountScreen(
            accountNavigationCallbacks = appState.accountNavigation,
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
            navController = appState.mainNavController,
        )
        hashTagScreen(
            onCloseClicked = { appState.popBackStack() },
            postCardNavigation = appState.postCardNavigation,
        )

        bottomTabScreen(appState)
    }
}

fun NavGraphBuilder.splashScreen() {
    composable(route = Routes.SPLASH) {
        SplashScreen()
    }
}

fun NavGraphBuilder.bottomTabScreen(appState: AppState) {
    composable(
        route = NavigationDestination.Feed.route,
    ) {
        val context = LocalContext.current
        val currentDestination = appState.currentNavigationDestination.collectAsState().value

        Scaffold(
            modifier = Modifier,
            snackbarHost = {
                MoSoSnackbarHost(appState.snackbarHostState) { snackbarData, snackbarType ->
                    MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
                }
            },
            floatingActionButton = {
                when (currentDestination) {
                    NavigationDestination.Feed -> {
                        MoSoFloatingActionButton(onClick = appState::navigateToNewPost) {
                            Icon(
                                MoSoIcons.plus(),
                                stringResource(id = R.string.feed_fab_content_description)
                            )
                        }
                    }

                    else -> {}
                }
            },
            bottomBar = {
                BottomBar(
                    currentDestination = currentDestination,
                    navigateToTopLevelDestination = appState::navigateToBottomBarDestination
                )
            },
            content = {
                BottomTabNavHost(
                    modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                    appState = appState,
                    context = context,
                )
            }
        )


    }
}

@Composable
private fun BottomBar(
    currentDestination: NavigationDestination?,
    navigateToTopLevelDestination: (route: NavigationDestination) -> Unit,
) {
    // don't show bottom bar if our current route is not one of the bottom nav options
    if (BottomBarTabs.values()
            .find { it.bottomBarTab.navigationDestination == currentDestination } == null
    ) {
        return
    }

    currentDestination?.let {
        MoSoBottomNavigationBar(
            currentDestination = currentDestination,
            bottomBarTabs = BottomBarTabs.values().map { it.bottomBarTab },
            navigateTo = navigateToTopLevelDestination
        )
    }
}