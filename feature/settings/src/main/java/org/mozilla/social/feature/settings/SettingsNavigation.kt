package org.mozilla.social.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.feature.settings.about.AboutSettingsScreen
import org.mozilla.social.feature.settings.account.AccountSettingsScreen
import org.mozilla.social.feature.settings.privacy.PrivacySettingsScreen

fun NavGraphBuilder.settingsFlow() {
    navigation(
        startDestination = SettingsNavigationDestination.MainSettings.route,
        route = NavigationDestination.Settings.route,
    ) {
        mainSettingsScreen()
        accountSettingsScreen()
        privacySettingsScreen()
        aboutSettingsScreen()
    }
}

fun NavGraphBuilder.mainSettingsScreen() {
    composable(route = SettingsNavigationDestination.MainSettings.route) {
        SettingsScreen()
    }
}

fun NavGraphBuilder.accountSettingsScreen() {
    composable(route = SettingsNavigationDestination.AccountSettings.route) {
        AccountSettingsScreen()
    }
}

fun NavGraphBuilder.privacySettingsScreen() {
    composable(route = SettingsNavigationDestination.PrivacySettings.route) {
        PrivacySettingsScreen()
    }
}

fun NavGraphBuilder.aboutSettingsScreen() {
    composable(route = SettingsNavigationDestination.AboutSettings.route) {
        AboutSettingsScreen()
    }
}