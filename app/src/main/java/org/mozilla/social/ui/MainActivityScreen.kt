package org.mozilla.social.ui

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
import org.mozilla.social.core.designsystem.component.MoSoScaffold
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.icon.mozillaLogo
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.navigation.MozillaNavHost
import org.mozilla.social.navigation.BottomBarTabs
import org.mozilla.social.ui.navigationdrawer.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    val appState = rememberAppState()

    val currentRoute = appState.currentRoute.collectAsState().value

    MoSoScaffold(
        modifier = Modifier.nestedScroll(appState.topAppBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { appState.snackbarHostState },
        floatingActionButton = {
            FloatingActionButton(
                currentRoute = currentRoute,
                onClick = appState::navigateToNewPost
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = currentRoute,
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination
            )
        },
        topBar = {
            TopBar(
                currentRoute = currentRoute,
                topAppBarScrollBehavior = appState.topAppBarScrollBehavior,
                navigationDrawerState = appState.navigationDrawerState,
            )
        },
        topAppBarScrollBehavior = appState.topAppBarScrollBehavior,
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
                MozillaNavHost(appState = appState)
            }
        }
    )
}

@Composable
private fun FloatingActionButton(
    currentRoute: String,
    onClick: () -> Unit,
) {
    when (currentRoute) {
        NavigationDestination.Feed.route -> {
            androidx.compose.material3.FloatingActionButton(onClick = onClick) {
                Icon(
                    MoSoIcons.Add,
                    stringResource(id = R.string.feed_fab_content_description)
                )
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    currentRoute: String,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    navigationDrawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    when (currentRoute) {
        BottomBarTabs.FEED.bottomBarTab.route,
        BottomBarTabs.DISCOVER.bottomBarTab.route,
        BottomBarTabs.BOOKMARKS.bottomBarTab.route -> {
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
        else -> {}
    }
}

@Composable
private fun BottomBar(
    currentRoute: String,
    navigateToTopLevelDestination: (route: String) -> Unit,
) {
    // don't show bottom bar if our current route is not one of the bottom nav options
    if (BottomBarTabs.values().find { it.bottomBarTab.route == currentRoute } == null) {
        return
    }

    MoSoBottomNavigationBar(
        currentRoute = currentRoute,
        bottomBarTabs = BottomBarTabs.values().map { it.bottomBarTab },
        navigateTo = navigateToTopLevelDestination
    )
}

@Composable
private fun BottomSheetContent() {
    Text(text = "feed options coming")
}