package org.mozilla.social.feature.followers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToFollowing(
    accountId: String,
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.Following.route(accountId), navOptions)
}

fun NavGraphBuilder.followingScreen() {
    composable(
        route = NavigationDestination.Following.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Following.NAV_PARAM_ACCOUNT_ID) {
                nullable = false
            }
        )
    ) {
        val accountId: String = it.arguments?.getString(NavigationDestination.Following.NAV_PARAM_ACCOUNT_ID)!!
        FollowersScreen(
            accountId = accountId,
            followersScreenType = FollowerScreenType.FOLLOWING,
        )
    }
}