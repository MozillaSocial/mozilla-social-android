package org.mozilla.social.feature.settings

interface SettingsInteractions {
    fun onScreenViewed()
    fun onAboutClicked()
    fun onAccountClicked()
    fun onContentPreferencesClicked()
    fun onPrivacyClicked()
    fun onDeveloperOptionsClicked()
    fun onOpenSourceLicensesClicked()
}
