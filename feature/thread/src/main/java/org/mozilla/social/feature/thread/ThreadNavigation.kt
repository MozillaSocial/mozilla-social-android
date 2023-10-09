package org.mozilla.social.feature.thread

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.PostCardNavigation

fun NavController.navigateToThread(
    navOptions: NavOptions? = null,
    threadStatusId: String,
) {
    navigate(NavigationDestination.Thread.route(threadStatusId), navOptions)
}

fun NavGraphBuilder.threadScreen(
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
) {
    composable(
        route = NavigationDestination.Thread.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Thread.NAV_PARAM_STATUS_ID) {
                nullable = true
            }
        )
    ) {
        val threadStatusId: String? = it.arguments?.getString(NavigationDestination.Thread.NAV_PARAM_STATUS_ID)
        threadStatusId?.let {
            ThreadScreen(
                threadStatusId = threadStatusId,
                onCloseClicked = onCloseClicked,
                postCardNavigation = postCardNavigation,
            )
        }
    }
}