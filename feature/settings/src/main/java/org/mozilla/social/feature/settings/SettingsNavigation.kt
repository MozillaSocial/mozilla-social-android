package org.mozilla.social.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(NavigationDestination.Settings.route, navOptions)
}

fun NavGraphBuilder.settingsScreen(onLogout: () -> Unit) {
    composable(route = NavigationDestination.Settings.route) {
        SettingsScreen(onLogout = onLogout)
    }
}
