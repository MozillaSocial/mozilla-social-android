package org.mozilla.social.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val NEW_POST_ROUTE = "auth"

fun NavController.navigateToNewPost(navOptions: NavOptions? = null) {
    this.navigate(NEW_POST_ROUTE, navOptions)
}

fun NavGraphBuilder.newPostScreen() {
    composable(route = NEW_POST_ROUTE) {
        NewPostRoute()
    }
}