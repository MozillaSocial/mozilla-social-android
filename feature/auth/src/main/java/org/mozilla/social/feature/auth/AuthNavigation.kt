package org.mozilla.social.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    this.navigate(NavigationDestination.Auth.route, navOptions)
}

fun NavGraphBuilder.loginScreen(navigateToLoggedInGraph: () -> Unit) {
    composable(route = NavigationDestination.Auth.route) {
        LoginScreen(navigateToLoggedInGraph = navigateToLoggedInGraph)
    }
}