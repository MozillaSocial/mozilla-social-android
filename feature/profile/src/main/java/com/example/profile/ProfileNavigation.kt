package com.example.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val Profile_ROUTE = "Profile-route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(Profile_ROUTE, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(route = Profile_ROUTE) {
    }
}