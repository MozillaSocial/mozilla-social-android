package org.mozilla.social.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val AUTH_ROUTE = "auth"

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    this.navigate(AUTH_ROUTE, navOptions)
}

fun NavGraphBuilder.loginScreen(navigateToLoggedInGraph: () -> Unit) {
    composable(route = AUTH_ROUTE) {
        LoginScreen(navigateToLoggedInGraph = navigateToLoggedInGraph)
    }
}