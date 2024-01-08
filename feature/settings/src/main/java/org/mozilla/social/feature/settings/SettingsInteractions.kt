package org.mozilla.social.feature.settings

interface SettingsInteractions {
    fun onScreenViewed() = Unit
    fun onAboutClicked() = Unit
    fun onAccountClicked() = Unit
    fun onContentPreferencesClicked() = Unit
    fun onPrivacyClicked() = Unit
    fun onDeveloperOptionsClicked() = Unit
}
