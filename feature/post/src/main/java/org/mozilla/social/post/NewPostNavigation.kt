package org.mozilla.social.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToNewPost(
    navOptions: NavOptions? = null,
    replyStatusId: String? = null,
) {
    navigate(NavigationDestination.NewPost.route(replyStatusId), navOptions)
}

fun NavGraphBuilder.newPostScreen(
    onStatusPosted: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = NavigationDestination.NewPost.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID) {
                nullable = true
            }
        )
    ) {
        val replyStatusId: String? = it.arguments?.getString(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID)
        NewPostScreen(
            onStatusPosted,
            onCloseClicked,
            replyStatusId = replyStatusId
        )
    }
}