package org.mozilla.social.feature.settings

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class SettingsAnalytics(private val analytics: Analytics) {
    fun settingsImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_SCREEN_IMPRESSION,
        )
    }

    fun accountSettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_ACCOUNT_IMPRESSION,
        )
    }

    fun logoutEngagement() {
        analytics.uiEngagement(
            uiIdentifier = SETTINGS_ACCOUNT_SIGNOUT,
        )
    }

    fun collectDataEngagement(value: Boolean) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PRIVACY_COLLECT_DATA_TOGGLE,
            engagementValue = value.toString(),
        )
    }

    fun privacySettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_PRIVACY_IMPRESSION,
        )
    }

    fun contentPreferencesSettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_CONTENT_PREFERENCES_IMPRESSION,
        )
    }

    fun openSourceLicencesImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_CONTENT_OPEN_SOURCE_LICENSE,
        )
    }

    fun aboutSettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_ABOUT_IMPRESSION,
        )
    }

    fun mutedUsersSettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = MUTED_USERS_SCREEN_IMPRESSION,
        )
    }

    fun blockedUsersSettingsImpression() {
        analytics.uiImpression(
            uiIdentifier = BLOCKED_USERS_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val SETTINGS_SCREEN_IMPRESSION = "settings.screen.impression"
        private const val SETTINGS_ACCOUNT_IMPRESSION = "settings.account.impression"
        private const val SETTINGS_CONTENT_PREFERENCES_IMPRESSION = "settings.content-preferences.impression"
        private const val SETTINGS_CONTENT_OPEN_SOURCE_LICENSE = "settings.open-source-licenses.impression"
        private const val MUTED_USERS_SCREEN_IMPRESSION = "muted.users.screen.impression"
        private const val BLOCKED_USERS_SCREEN_IMPRESSION = "blocked.users.screen.impression"
        private const val SETTINGS_PRIVACY_IMPRESSION = "settings.privacy.impression"
        private const val SETTINGS_ABOUT_IMPRESSION = "settings.about.impression"
        private const val PRIVACY_COLLECT_DATA_TOGGLE = "privacy.collect-data.toggle"
        private const val SETTINGS_ACCOUNT_SIGNOUT = "account.signout"

    }
}