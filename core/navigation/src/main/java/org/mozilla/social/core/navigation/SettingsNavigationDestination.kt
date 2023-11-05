package org.mozilla.social.core.navigation

import androidx.navigation.NavController

sealed class SettingsNavigationDestination(val route: String) {
    data object AccountSettings : SettingsNavigationDestination(
        route = "accountSettings"
    ) {
        fun NavController.navigateToAccountSettings() {
            navigate(route = route)
        }
    }

    data object PrivacySettings : SettingsNavigationDestination(
        route = "privacySettings"
    ) {
        fun NavController.navigateToPrivacySettings() {
            navigate(route = route)
        }
    }

    data object AboutSettings : SettingsNavigationDestination(
        route = "aboutSettings"
    ) {
        fun NavController.navigateToAboutSettings() {
            navigate(route = route)
        }
    }
}