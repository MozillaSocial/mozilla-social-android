package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACCOUNT_ROUTE = "account-route"

fun NavController.navigateToAccount(navOptions: NavOptions? = null) {
    this.navigate(ACCOUNT_ROUTE, navOptions)
}

fun NavGraphBuilder.accountScreen(
    userFollowing: () -> Unit,
    userFollowers: () -> Unit,
    onLogout: () -> Unit
) {
    composable(route = ACCOUNT_ROUTE) {
        AccountRoute(
            onUserFollowings = userFollowing,
            onUserFollowers = userFollowers,
            onLogout = onLogout
        )
    }
}
