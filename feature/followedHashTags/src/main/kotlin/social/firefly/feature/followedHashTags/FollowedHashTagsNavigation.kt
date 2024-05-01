package social.firefly.feature.followedHashTags

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.followedHashTagsScreen() {
    composable(route = NavigationDestination.FollowedHashTags.route) {
        FollowedHashTagsScreen()
    }
}