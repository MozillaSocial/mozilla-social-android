package org.mozilla.social.feature.account

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
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
            windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        )
    }
}