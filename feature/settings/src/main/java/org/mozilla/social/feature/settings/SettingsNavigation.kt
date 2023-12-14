package org.mozilla.social.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.feature.settings.about.AboutSettingsScreen
import org.mozilla.social.feature.settings.account.AccountSettingsScreen
import org.mozilla.social.feature.settings.contentpreferences.ContentPreferencesSettingsScreen
import org.mozilla.social.feature.settings.contentpreferences.blockedusers.BlockedUsersSettingsScreen
import org.mozilla.social.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsScreen
import org.mozilla.social.feature.settings.licenses.OpenSourceLicensesScreen
import org.mozilla.social.feature.settings.privacy.PrivacySettingsScreen

fun NavGraphBuilder.settingsFlow() {
    navigation(
        startDestination = SettingsNavigationDestination.MainSettings.route,
        route = NavigationDestination.Settings.route,
    ) {
        mainSettingsScreen()
        accountSettingsScreen()
        contentPreferencesSettingsScreen()
        mutedUsersSettingsScreen()
        blockedUsersSettingsScreen()
        privacySettingsScreen()
        aboutSettingsScreen()
        openSourceLicensesScreen()
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

fun NavGraphBuilder.contentPreferencesSettingsScreen() {
    composable(route = SettingsNavigationDestination.ContentPreferencesSettings.route) {
        ContentPreferencesSettingsScreen()
    }
}

fun NavGraphBuilder.mutedUsersSettingsScreen() {
    composable(route = SettingsNavigationDestination.MutedUsersSettings.route) {
        MutedUsersSettingsScreen()
    }
}

fun NavGraphBuilder.blockedUsersSettingsScreen() {
    composable(route = SettingsNavigationDestination.BlockedUsersSettings.route) {
        BlockedUsersSettingsScreen()
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

fun NavGraphBuilder.openSourceLicensesScreen() {
    composable(route = SettingsNavigationDestination.OpenSourceLicensesSettings.route) {
        OpenSourceLicensesScreen()
    }
}
