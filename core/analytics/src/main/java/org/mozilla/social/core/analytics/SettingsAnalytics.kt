package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class SettingsAnalytics internal constructor(private val analytics: Analytics) {
    fun settingsScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_SCREEN_IMPRESSION,
        )
    }

    fun accountSettingsViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_ACCOUNT_IMPRESSION,
        )
    }

    fun logoutClicked() {
        analytics.uiEngagement(
            uiIdentifier = SETTINGS_ACCOUNT_SIGNOUT,
        )
    }

    fun collectDataToggled(value: Boolean) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PRIVACY_COLLECT_DATA_TOGGLE,
            engagementValue = value.toString(),
        )
    }

    fun privacySettingsViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_PRIVACY_IMPRESSION,
        )
    }

    fun contentPreferencesSettingsViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_CONTENT_PREFERENCES_IMPRESSION,
        )
    }

    fun openSourceLicencesViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_CONTENT_OPEN_SOURCE_LICENSE,
        )
    }

    fun aboutSettingsViewed() {
        analytics.uiImpression(
            uiIdentifier = SETTINGS_ABOUT_IMPRESSION,
        )
    }

    fun mutedUsersSettingsViewed() {
        analytics.uiImpression(
            uiIdentifier = MUTED_USERS_SCREEN_IMPRESSION,
        )
    }

    fun blockedUsersSettingsViewed() {
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