package org.mozilla.social.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.MoSoAppBar
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoFloatingActionButton
import org.mozilla.social.core.designsystem.component.MoSoScaffold
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.icon.mozillaLogo
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.navigation.MozillaNavHost
import org.mozilla.social.navigation.BottomBarTabs
import org.mozilla.social.ui.navigationdrawer.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(context: Context) {
    val appState = rememberAppState()

    val currentDestination = appState.currentNavigationDestination.collectAsState().value

    MoSoScaffold(
        modifier = if (AppState.shouldShowTopBar(currentDestination)) {
            Modifier.nestedScroll(appState.topAppBarScrollBehavior.nestedScrollConnection)
        } else {
            Modifier
        },
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
        topBar = {
            TopBar(
                currentDestination = currentDestination,
                topAppBarScrollBehavior = appState.topAppBarScrollBehavior,
                navigationDrawerState = appState.navigationDrawerState,
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
            Box(modifier = Modifier.padding(it)) {
                MozillaNavHost(appState = appState, context = context)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    currentDestination: NavigationDestination?,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    navigationDrawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    if (AppState.shouldShowTopBar(currentDestination)) {
        Column {
            MoSoAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    Image(
                        painter = mozillaLogo(),
                        contentDescription = "mozilla logo"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            navigationDrawerState.open()
                        }
                    }) {
                        Icon(
                            painter = MoSoIcons.list(),
                            modifier = Modifier.size(24.dp),
                            contentDescription = stringResource(id = R.string.navigation_menu_content_description),
                        )
                    }
                },
                actions = {}
            )

            MoSoDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(MoSoTheme.colors.borderPrimary)
            )
        }
    }
}

@Composable
private fun BottomBar(
    currentDestination: NavigationDestination?,
    navigateToTopLevelDestination: (route: NavigationDestination) -> Unit,
) {
    // don't show bottom bar if our current route is not one of the bottom nav options
    if (BottomBarTabs.values().find { it.bottomBarTab.navigationDestination == currentDestination } == null) {
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