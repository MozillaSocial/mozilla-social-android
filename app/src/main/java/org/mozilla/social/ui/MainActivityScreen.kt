package org.mozilla.social.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.mozilla.social.navigation.MozillaNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    val appState = rememberAppState()

    Scaffold(
        modifier = Modifier.nestedScroll(appState.topAppBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { appState.snackbarHostState },
        floatingActionButton = { appState.floatingActionButton() },
        bottomBar = { appState.bottomBar() },
        topBar = { appState.topBar() },
    ) {

        Box(modifier = Modifier.padding(it)) {
            MozillaNavHost(appState = appState)
        }
    }
}