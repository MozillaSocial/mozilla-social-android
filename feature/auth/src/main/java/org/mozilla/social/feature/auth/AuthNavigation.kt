package org.mozilla.social.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val AUTH_ROUTE = "auth"

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    this.navigate(AUTH_ROUTE, navOptions)
}

fun NavGraphBuilder.authScreen() {
    composable(route = AUTH_ROUTE) {
        AuthRoute()
    }
}