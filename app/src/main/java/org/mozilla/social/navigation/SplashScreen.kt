package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.domain.IsSignedInFlow

@Composable
fun SplashScreenRoute(
    viewModel: SplashViewModel = koinViewModel(),
    navigateToLogin: () -> Unit,
    navigateToLoggedInGraph: () -> Unit,
) {
    when (viewModel.isSignedIn.collectAsState(initial = null).value) {
        null -> {
            LoadingScreen()
        }

        true -> {
            navigateToLoggedInGraph()
        }

        false -> {
            navigateToLogin()
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

const val SPLASH_ROUTE = "splash"