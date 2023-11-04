package org.mozilla.social.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.settingsScreen() {
    composable(route = NavigationDestination.Settings.route) {
        SettingsScreen()
    }
}
