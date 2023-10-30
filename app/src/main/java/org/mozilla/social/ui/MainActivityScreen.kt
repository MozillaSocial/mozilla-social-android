package org.mozilla.social.ui

import androidx.compose.runtime.Composable
import org.mozilla.social.navigation.MainNavHost

@Composable
fun MainActivityScreen() {
    val appState = rememberAppState()

    MainNavHost(
        appState = appState,
    )
}