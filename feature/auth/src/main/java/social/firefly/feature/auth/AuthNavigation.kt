package social.firefly.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.NavigationDestination
import social.firefly.feature.auth.chooseServer.ChooseServerScreen
import social.firefly.feature.auth.login.LoginScreen

fun NavGraphBuilder.authFlow() {
    navigation(
        startDestination = AuthNavigationDestination.Login.route,
        route = NavigationDestination.Auth.route,
    ) {
        loginScreen()
        chooseServerScreen()
    }
}

fun NavGraphBuilder.loginScreen() {
    composable(route = AuthNavigationDestination.Login.route) {
        LoginScreen()
    }
}

fun NavGraphBuilder.chooseServerScreen() {
    composable(route = AuthNavigationDestination.ChooseServer.route) {
        ChooseServerScreen()
    }
}
