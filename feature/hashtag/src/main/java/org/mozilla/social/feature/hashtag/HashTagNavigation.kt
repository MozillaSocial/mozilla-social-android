package org.mozilla.social.feature.hashtag

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.PostCardNavigation

fun NavController.navigateToHashTag(
    navOptions: NavOptions? = null,
    hashTagValue: String,
) {
    navigate(NavigationDestination.HashTag.route(hashTagValue), navOptions)
}

fun NavGraphBuilder.hashTagScreen(
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
) {
    composable(
        route = NavigationDestination.HashTag.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.HashTag.NAV_PARAM_HASH_TAG) {
                nullable = true
            }
        )
    ) {
        val hashTagValue: String? = it.arguments?.getString(NavigationDestination.HashTag.NAV_PARAM_HASH_TAG)
        hashTagValue?.let {
            HashTagScreen(
                hashTag = hashTagValue,
                postCardNavigation = postCardNavigation,
                onCloseClicked = onCloseClicked,
            )
        }
    }
}