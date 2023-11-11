package org.mozilla.social.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.mozilla.social.core.navigation.AuthNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.auth.chooseServer.ChooseServerScreen
import org.mozilla.social.feature.auth.login.LoginScreen

fun NavGraphBuilder.authFlow() {
    navigation(
        startDestination = AuthNavigationDestination.Login.route,
        route = NavigationDestination.Auth.route
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