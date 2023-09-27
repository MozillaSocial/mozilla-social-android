package org.mozilla.social.feature.followers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val FOLLOWERS_ROUTE = "followers"
private const val ACCOUNT_ID = "accountId"
private const val FOLLOWERS_FULL_ROUTE = "$FOLLOWERS_ROUTE?$ACCOUNT_ID={$ACCOUNT_ID}"

fun NavController.navigateToFollowers(
    accountId: String,
    navOptions: NavOptions? = null,
) {
    navigate("$FOLLOWERS_ROUTE?$ACCOUNT_ID=$accountId", navOptions)
}

fun NavGraphBuilder.followersScreen(
    followersNavigationCallbacks: FollowersNavigationCallbacks,
) {
    composable(
        route = FOLLOWERS_FULL_ROUTE,
        arguments = listOf(
            navArgument(ACCOUNT_ID) {
                nullable = false
            }
        )
    ) {
        val accountId: String = it.arguments?.getString(ACCOUNT_ID)!!
        FollowersRoute(
            accountId = accountId,
            followersNavigationCallbacks = followersNavigationCallbacks,
        )
    }
}