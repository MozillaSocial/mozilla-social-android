package org.mozilla.social.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val REPLY_STATUS_ID = "replyStatusId"
const val NEW_POST_ROUTE = "newPost"
const val NEW_POST_FULL_ROUTE = "$NEW_POST_ROUTE?$REPLY_STATUS_ID={$REPLY_STATUS_ID}"

fun NavController.navigateToNewPost(
    navOptions: NavOptions? = null,
    replyStatusId: String? = null,
) {
    when {
        replyStatusId != null -> navigate("newPost?replyStatusId=$replyStatusId", navOptions)
        else -> this.navigate(NEW_POST_ROUTE, navOptions)
    }
}

fun NavGraphBuilder.newPostScreen(
    onStatusPosted: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = NEW_POST_FULL_ROUTE,
        arguments = listOf(
            navArgument(REPLY_STATUS_ID) {
                nullable = true
            }
        )
    ) {
        val replyStatusId: String? = it.arguments?.getString(REPLY_STATUS_ID)
        NewPostRoute(
            onStatusPosted,
            onCloseClicked,
            replyStatusId = replyStatusId
        )
    }
}