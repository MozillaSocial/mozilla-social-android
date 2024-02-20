package social.firefly.post

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import social.firefly.common.utils.ffSlideIn
import social.firefly.common.utils.ffSlideOut
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.newPostScreen() {
    composable(
        route = NavigationDestination.NewPost.fullRoute,
        arguments =
        listOf(
            navArgument(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID) {
                nullable = true
            },
            navArgument(NavigationDestination.NewPost.NAV_PARAM_EDIT_STATUS_ID) {
                nullable = true
            },
        ),
        enterTransition = { ffSlideIn() },
        exitTransition = { ffSlideOut() },
    ) {
        val replyStatusId: String? =
            it.arguments?.getString(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID)
        val editStatusId: String? =
            it.arguments?.getString(NavigationDestination.NewPost.NAV_PARAM_EDIT_STATUS_ID)
        social.firefly.post.NewPostScreen(
            replyStatusId = replyStatusId,
            editStatusId = editStatusId
        )
    }
}
