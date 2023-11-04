package org.mozilla.social.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.common.utils.mosoSlideIn
import org.mozilla.social.common.utils.mosoSlideOut
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.newPostScreen() {
    composable(
        route = NavigationDestination.NewPost.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID) {
                nullable = true
            }
        ),
        enterTransition = { mosoSlideIn() },
        exitTransition = { mosoSlideOut() }
    ) {
        val replyStatusId: String? =
            it.arguments?.getString(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID)
        NewPostScreen(replyStatusId = replyStatusId)
    }
}