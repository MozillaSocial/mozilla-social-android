package social.firefly.feature.followers

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.followersScreen() {
    composable(
        route = NavigationDestination.Followers.fullRoute,
        arguments =
        listOf(
            navArgument(NavigationDestination.Followers.NAV_PARAM_ACCOUNT_ID) {
                nullable = false
            },
            navArgument(NavigationDestination.Followers.NAV_PARAM_DISPLAY_NAME) {
                nullable = false
            },
            navArgument(NavigationDestination.Followers.NAV_PARAM_STARTING_TAB) {
                nullable = false
            },
        ),
    ) {
        val accountId: String =
            it.arguments?.getString(NavigationDestination.Followers.NAV_PARAM_ACCOUNT_ID)!!
        val displayName: String =
            it.arguments?.getString(NavigationDestination.Followers.NAV_PARAM_DISPLAY_NAME)!!
        val startingTab: String =
            it.arguments?.getString(NavigationDestination.Followers.NAV_PARAM_STARTING_TAB)!!
        FollowersScreen(
            accountId = accountId,
            displayName = displayName,
            startingTab = when (startingTab) {
                NavigationDestination.Followers.StartingTab.FOLLOWERS.value -> FollowType.FOLLOWERS
                else -> FollowType.FOLLOWING
            }
        )
    }
}
