package org.mozilla.social.feature.media

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.mediaScreen() {
    composable(
        route = NavigationDestination.Media.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Media.NAV_PARAM_BUNDLE) {
                nullable = false
            },
        ),
    ) {
        val bundle: String =
            it.arguments?.getString(
                NavigationDestination.Media.NAV_PARAM_BUNDLE,
            )!!

        val deserializedBundle: NavigationDestination.Media.MediaBundle = Json.decodeFromString(bundle)

        MediaScreen(mediaBundle = deserializedBundle)
    }
}
