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
import org.mozilla.social.core.domain.IsSignedInFlow

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    navigateToLogin: () -> Unit,
    navigateToLoggedInGraph: () -> Unit,
) {
    when (viewModel.isSignedIn.collectAsState(initial = null).value) {
        null -> {
            LoadingScreen()
        }

        true -> {
            // TODO@DA remove these
            LaunchedEffect(Unit) {
                navigateToLoggedInGraph()
            }
        }

        false -> {
            LaunchedEffect(Unit) {
                navigateToLogin()
            }
        }
    }
}


@Composable
fun LoadingScreen() {

}

class SplashViewModel(isSignedInFlow: IsSignedInFlow) : ViewModel() {
    val isSignedIn: Flow<Boolean> = isSignedInFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialValue = false
    )
}

