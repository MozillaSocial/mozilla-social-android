package org.mozilla.social.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val REPLY_ID = "replyId"
const val NEW_POST_ROUTE = "newPost"
const val NEW_POST_FULL_ROUTE = "$NEW_POST_ROUTE?$REPLY_ID={$REPLY_ID}"

fun NavController.navigateToNewPost(
    navOptions: NavOptions? = null,
    replyId: String? = null,
) {
    when {
        replyId != null -> navigate("newPost?replyId=$replyId", navOptions)
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
            navArgument(REPLY_ID) {
                nullable = true
            }
        )
    ) {
        val replyId: String? = it.arguments?.getString(REPLY_ID)
        NewPostRoute(onStatusPosted, onCloseClicked, replyId)
    }
}