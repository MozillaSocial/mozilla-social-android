package org.mozilla.social.ui.bottombar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.ui.common.divider.MoSoDivider
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
fun BottomBarTabScreen(appState: AppState) {
    appState.tabbedNavController = rememberNavController()
    val currentDestination by appState.currentBottomBarNavigationDestination.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
    ) {
        val tabbedNavController by appState.tabbedNavControllerFlow.collectAsStateWithLifecycle()
        tabbedNavController?.let {
            BottomTabNavHost(
                modifier = Modifier.weight(1f),
                tabbedNavController = it,
            )
        }
        currentDestination?.let {
            BottomBar(
                currentDestination = it,
            )
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    currentDestination: BottomBarNavigationDestination,
    navigateTo: NavigateTo = koinInject(),
) {
    Column(
        modifier = modifier,
    ) {
        MoSoDivider()
        MoSoBottomNavigationBar(
            currentDestination = currentDestination,
            bottomBarTabs = BottomBarTabs.entries.map { it.bottomBarTab },
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
