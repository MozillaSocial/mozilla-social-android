package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToMyAccount(
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.MyAccount.route, navOptions)
}

fun NavGraphBuilder.myAccountScreen(
) {
    composable(
        route = NavigationDestination.MyAccount.route,
    ) {
        AccountScreen(
            accountId = null,
        )
    }
}