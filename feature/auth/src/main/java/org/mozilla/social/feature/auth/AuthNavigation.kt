package org.mozilla.social.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.loginScreen() {
    composable(route = NavigationDestination.Login.route) {
        LoginScreen()
    }
}