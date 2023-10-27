package org.mozilla.social.post

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500),
            )
        }
    ) {
        val replyStatusId: String? =
            it.arguments?.getString(NavigationDestination.NewPost.NAV_PARAM_REPLY_STATUS_ID)
        NewPostScreen(
            onStatusPosted,
            onCloseClicked,
            replyStatusId = replyStatusId
        )
    }
}