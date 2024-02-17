package social.firefly.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.feature.settings.about.AboutSettingsScreen
import social.firefly.feature.settings.account.AccountSettingsScreen
import social.firefly.feature.settings.contentpreferences.ContentPreferencesSettingsScreen
import social.firefly.feature.settings.contentpreferences.blockedusers.BlockedUsersSettingsScreen
import social.firefly.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsScreen
import social.firefly.feature.settings.developer.DeveloperOptionsScreen
import social.firefly.feature.settings.licenses.OpenSourceLicensesScreen
import social.firefly.feature.settings.privacy.PrivacySettingsScreen

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
        developerOptionsScreen()
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

fun NavGraphBuilder.developerOptionsScreen() {
    composable(route = SettingsNavigationDestination.DeveloperOptions.route) {
        DeveloperOptionsScreen()
    }
}
