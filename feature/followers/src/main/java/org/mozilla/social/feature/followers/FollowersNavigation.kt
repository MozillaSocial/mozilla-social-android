package org.mozilla.social.feature.followers

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.followersScreen() {
    composable(
        route = NavigationDestination.Followers.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Followers.NAV_PARAM_ACCOUNT_ID) {
                nullable = false
            }
        )
    ) {
        val accountId: String = it.arguments?.getString(NavigationDestination.Followers.NAV_PARAM_ACCOUNT_ID)!!
        FollowersScreen(
            accountId = accountId,
            startingTab = FollowType.FOLLOWERS,
        )
    }
}
