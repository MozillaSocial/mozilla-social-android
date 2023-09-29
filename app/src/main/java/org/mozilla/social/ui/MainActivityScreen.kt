package org.mozilla.social.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.mozilla.social.core.designsystem.component.MoSoScaffold
import org.mozilla.social.navigation.MozillaNavHost
import org.mozilla.social.ui.navigationdrawer.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    val appState = rememberAppState()

    MoSoScaffold(
        modifier = Modifier.nestedScroll(appState.topAppBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { appState.snackbarHostState },
        floatingActionButton = { appState.FloatingActionButton() },
        bottomBar = { appState.BottomBar() },
        topBar = { appState.TopBar() },
        topAppBarScrollBehavior = appState.topAppBarScrollBehavior,
        navigationDrawerState = appState.navigationDrawerState,
        navigationDrawerContent = {
            NavigationDrawer(
                onSettingsClicked = appState::navigateToSettings,
                onLoggedOut = appState::navigateToLoginScreen,
            )
        },
        bottomSheetContent = { appState.BottomSheetContent() },
        bottomSheetVisible = appState.bottomSheetVisible.value,
        content = {
            Box(modifier = Modifier.padding(it)) {
                MozillaNavHost(appState = appState)
            }
        }
    )
}