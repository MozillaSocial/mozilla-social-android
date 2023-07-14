package org.mozilla.social.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val authRoute = "auth"

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(authRoute, navOptions)
}

fun NavGraphBuilder.authScreen(
    onLoginClicked: () -> Unit,
) {
    composable(route = authRoute) {
        AuthRoute(onLoginClicked)
    }
}