package org.mozilla.social.feature.account.follows

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACCOUNT_FOLLOWING_ROUTE = "account-following-route"
const val ACCOUNT_FOLLOWERS_ROUTE = "account-followers-route"

fun NavController.navigateToAccountFollowing(navOptions: NavOptions? = null) {
    this.navigate(ACCOUNT_FOLLOWING_ROUTE, navOptions)
}

fun NavController.navigateToAccountFollowers(navOptions: NavOptions? = null) {
    this.navigate(ACCOUNT_FOLLOWERS_ROUTE, navOptions)
}

fun NavGraphBuilder.accountFollowingScreen() {
    composable(route = ACCOUNT_FOLLOWING_ROUTE) {
        AccountFollowUsersScreen(followType = FollowType.FOLLOWING)
    }
}

fun NavGraphBuilder.accountFollowersScreen() {
    composable(route = ACCOUNT_FOLLOWERS_ROUTE) {
        AccountFollowUsersScreen(followType = FollowType.FOLLOWERS)
    }
}