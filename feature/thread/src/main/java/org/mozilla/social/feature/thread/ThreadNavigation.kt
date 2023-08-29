package org.mozilla.social.feature.thread

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val THREAD_ROUTE = "thread"

fun NavController.navigateToThread(navOptions: NavOptions? = null) {
    this.navigate(THREAD_ROUTE, navOptions)
}

fun NavGraphBuilder.threadScreen() {
    composable(route = THREAD_ROUTE) {
        ThreadScreen()
    }
}