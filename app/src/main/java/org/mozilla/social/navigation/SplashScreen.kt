package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigateTo

@Composable
fun SplashScreen() {
    LoadingScreen()
}


@Composable
fun LoadingScreen() {

}
