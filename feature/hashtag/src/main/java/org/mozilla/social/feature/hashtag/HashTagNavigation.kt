package org.mozilla.social.feature.hashtag

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val HASH_TAG_ROUTE = "hashTag"
const val HASH_TAG_VALUE = "hashTagValue"
const val HASHTAG_FULL_ROUTE = "$HASH_TAG_ROUTE?$HASH_TAG_VALUE={$HASH_TAG_VALUE}"

fun NavController.navigateToHashTag(
    navOptions: NavOptions? = null,
    hashTagValue: String,
) {
    navigate("$HASH_TAG_ROUTE?$HASH_TAG_VALUE=$hashTagValue", navOptions)
}

fun NavGraphBuilder.hashTagScreen(
    //TODO add status card nav interface
) {
    composable(
        route = HASHTAG_FULL_ROUTE,
        arguments = listOf(
            navArgument(HASH_TAG_VALUE) {
                nullable = true
            }
        )
    ) {
        val hashTagValue: String? = it.arguments?.getString(HASH_TAG_VALUE)
        hashTagValue?.let {
            HashTagRoute(
                hashTag = hashTagValue,
            )
        }
    }
}