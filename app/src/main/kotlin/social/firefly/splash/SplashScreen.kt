package social.firefly.splash

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import social.firefly.ui.bottombar.Routes

fun NavGraphBuilder.splashScreen() {
    composable(route = Routes.SPLASH) {
        SplashScreen()
    }
}

@Composable
private fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel()
) {
    val activity = LocalContext.current as? ComponentActivity
    LaunchedEffect(Unit) {
        viewModel.initialize(activity?.intent)
    }
}