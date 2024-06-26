package social.firefly.core.navigation

import androidx.navigation.NavController

sealed class SettingsNavigationDestination(val route: String) {
    data object MainSettings : SettingsNavigationDestination(
        route = "mainSettings",
    ) {
        fun NavController.navigateToMainSettings() {
            navigate(route = route)
        }
    }

    data object AccountSettings : SettingsNavigationDestination(
        route = "accountSettings",
    ) {
        fun NavController.navigateToAccountSettings() {
            navigate(route = route)
        }
    }

    data object ContentPreferencesSettings : SettingsNavigationDestination(
        route = "contentPreferencesSettings",
    ) {
        fun NavController.navigateToContentPreferencesSettings() {
            navigate(route = route)
        }
    }

    data object BlockedDomains : SettingsNavigationDestination(
        route = "blockedDomains",
    ) {
        fun NavController.navigateToBlockedDomains() {
            navigate(route = route)
        }
    }

    data object BlockedUsersSettings : SettingsNavigationDestination(
        route = "blockedUsersSettings",
    ) {
        fun NavController.navigateToBlockedUsers() {
            navigate(route = route)
        }
    }

    data object MutedUsersSettings : SettingsNavigationDestination(route = "mutedUsersSettings") {
        fun NavController.navigateToMutedUsers() {
            navigate(route = route)
        }
    }

    data object PrivacySettings : SettingsNavigationDestination(
        route = "privacySettings",
    ) {
        fun NavController.navigateToPrivacySettings() {
            navigate(route = route)
        }
    }

    data object AboutSettings : SettingsNavigationDestination(
        route = "aboutSettings",
    ) {
        fun NavController.navigateToAboutSettings() {
            navigate(route = route)
        }
    }

    data object OpenSourceLicensesSettings : SettingsNavigationDestination(
        route = "openSourceLicensesSettings",
    ) {
        fun NavController.navigateToOpenSourceSettings() {
            navigate(route = route)
        }
    }

    data object DeveloperOptions : SettingsNavigationDestination(
        route = "developerOptions",
    ) {
        fun NavController.navigateToDeveloperOptions() {
            navigate(route = route)
        }
    }

    data object AppearanceAndBehaviorOptions : SettingsNavigationDestination(
        route = "appearanceAndBehaviorOptions",
    ) {
        fun NavController.navigateToAppearanceAndBehaviorOptions() {
            navigate(route = route)
        }
    }
}
