package org.mozilla.social.ui

import androidx.compose.runtime.Composable
import org.mozilla.social.MainViewModel
import org.mozilla.social.navigation.MainNavHost

@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    val appState = rememberAppState()

    MainNavHost(
        appState = appState,
    )
}