package social.firefly.feature.media

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.mediaScreen() {
    composable(
        route = NavigationDestination.Media.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Media.NAV_PARAM_STATUS_ID) {
                nullable = false
            },
            navArgument(NavigationDestination.Media.NAV_PARAM_START_INDEX) {
                nullable = false
            },
        ),
    ) {
        val statusId: String = it.arguments?.getString(
            NavigationDestination.Media.NAV_PARAM_STATUS_ID
        )!!

        val startIndex: Int = it.arguments?.getString(
            NavigationDestination.Media.NAV_PARAM_START_INDEX
        )!!.toInt()

        MediaScreen(
            statusId = statusId,
            startIndex = startIndex,
        )
    }
}
