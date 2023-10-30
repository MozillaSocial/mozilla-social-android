package org.mozilla.social.ui

import android.content.Context
import androidx.compose.runtime.Composable
import org.mozilla.social.MainViewModel
import org.mozilla.social.navigation.MainNavHost

@Composable
fun MainActivityScreen(
    context: Context,
    viewModel: MainViewModel, // This needs to stay here since the injection is lazy
) {

    val appState = rememberAppState()

    MainNavHost(
        appState = appState,
        context = context,
    )
}