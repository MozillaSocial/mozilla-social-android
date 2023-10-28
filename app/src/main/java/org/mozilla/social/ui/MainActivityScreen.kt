package org.mozilla.social.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.navigation.MainNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainActivityScreen(
    context: Context,
    navigationViewModel: NavigationViewModel = koinViewModel()
) {
    val appState = rememberAppState()

    LaunchedEffect(key1 = Unit) {
        navigationViewModel.navigationEvents.onEach {
            appState.navigate(it)
        }.launchIn(this)
    }

    MainNavHost(
        appState = appState,
        context = context,
    )
}