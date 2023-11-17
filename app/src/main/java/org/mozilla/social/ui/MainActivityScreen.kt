package org.mozilla.social.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mozilla.social.core.ui.common.snackbar.MoSoSnackbar
import org.mozilla.social.core.ui.common.snackbar.MoSoSnackbarHost
import org.mozilla.social.navigation.MainNavHost
import org.mozilla.social.ui.bottombar.MoSoNavigationBarDefaults.bottomBarPadding

/**
 * must pass in view model to ensure it gets instantiated
 */
@Composable
@Suppress("UnusedPrivateMember")
fun MainActivityScreen() {
    val appState = rememberAppState()

    MainNavHost(
        appState = appState,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        val currentDestination by appState.currentNavigationDestination.collectAsStateWithLifecycle()

        MoSoSnackbarHost(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .systemBarsPadding()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .bottomBarPadding(currentDestination),
            hostState = appState.snackbarHostState,
        ) { snackbarData, snackbarType ->
            MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
        }
    }
}
