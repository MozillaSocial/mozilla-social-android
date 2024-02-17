package social.firefly.feature.thread

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.threadScreen() {
    composable(
        route = NavigationDestination.Thread.fullRoute,
        arguments =
        listOf(
            navArgument(NavigationDestination.Thread.NAV_PARAM_STATUS_ID) {
                nullable = true
            },
        ),
    ) {
        val threadStatusId: String? =
            it.arguments?.getString(NavigationDestination.Thread.NAV_PARAM_STATUS_ID)
        threadStatusId?.let {
            ThreadScreen(
                threadStatusId = threadStatusId,
            )
        }
    }
}
