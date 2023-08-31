package org.mozilla.social.feature.thread

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val THREAD_ROUTE = "thread"
const val THREAD_STATUS_ID = "threadStatusId"
const val THREAD_FULL_ROUTE = "$THREAD_ROUTE?$THREAD_STATUS_ID={$THREAD_STATUS_ID}"

fun NavController.navigateToThread(
    navOptions: NavOptions? = null,
    threadStatusId: String? = null,
) {
    when {
        threadStatusId != null -> navigate("$THREAD_ROUTE?$THREAD_STATUS_ID=$threadStatusId", navOptions)
        else -> this.navigate(THREAD_ROUTE, navOptions)
    }
}

fun NavGraphBuilder.threadScreen(
    onReplyClicked: (String) -> Unit,
    onPostClicked: (String) -> Unit,
) {
    composable(
        route = THREAD_FULL_ROUTE,
        arguments = listOf(
            navArgument(THREAD_STATUS_ID) {
                nullable = true
            }
        )
    ) {
        val threadStatusId: String? = it.arguments?.getString(THREAD_STATUS_ID)
        threadStatusId?.let {
            ThreadScreen(
                threadStatusId = threadStatusId,
                onPostClicked = onPostClicked,
                onReplyClicked = onReplyClicked,
            )
        }
    }
}