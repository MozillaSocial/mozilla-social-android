package org.mozilla.social.ui

import android.content.Context
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.MoSoFloatingActionButton
import org.mozilla.social.core.designsystem.component.MoSoScaffold
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.navigation.BottomBarTabs
import org.mozilla.social.navigation.MozillaNavHost
import org.mozilla.social.ui.navigationdrawer.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainActivityScreen(context: Context) {
    val appState = rememberAppState()

    val currentDestination = appState.currentNavigationDestination.collectAsState().value

    MoSoScaffold(
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
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination
            )
        },
        navigationDrawerState = appState.navigationDrawerState,
        navigationDrawerContent = {
            NavigationDrawer(
                onSettingsClicked = appState::navigateToSettings,
                onLoggedOut = appState::navigateToLoginScreen,
            )
        },
        bottomSheetContent = { BottomSheetContent() },
        bottomSheetVisible = appState.bottomSheetVisible.value,
        content = {
            MozillaNavHost(appState = appState, context = context, Modifier.consumeWindowInsets(it))
        }
    )
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

@Composable
private fun BottomSheetContent() {
    Text(text = "feed options coming")
}