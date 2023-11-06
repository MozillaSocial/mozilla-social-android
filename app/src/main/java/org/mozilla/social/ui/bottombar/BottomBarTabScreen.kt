package org.mozilla.social.ui.bottombar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.navigation.BottomTabNavHost
import org.mozilla.social.ui.AppState


fun NavGraphBuilder.bottomTabScreen(appState: AppState) {
    composable(
        route = NavigationDestination.Tabs.route,
    ) {
        BottomBarTabScreen(appState = appState)
    }
}

@Composable
fun BottomBarTabScreen(
    appState: AppState,
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