package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.analytics.SettingsAnalytics
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class SettingsViewModel(
    private val analytics: SettingsAnalytics,
    private val navigateTo: NavigateTo,
) : ViewModel(), SettingsInteractions {

    override fun onAboutClicked() {
        navigateTo(SettingsNavigationDestination.AboutSettings)
    }

    override fun onAccountClicked() {
        navigateTo(SettingsNavigationDestination.AccountSettings)
    }

    override fun onContentPreferencesClicked() {
        navigateTo(SettingsNavigationDestination.ContentPreferencesSettings)
    }

    override fun onPrivacyClicked() {
        navigateTo(SettingsNavigationDestination.PrivacySettings)
    }

    override fun onDeveloperOptionsClicked() {
        navigateTo(SettingsNavigationDestination.DeveloperOptions)
    }

    override fun onOpenSourceLicensesClicked() {
        navigateTo(SettingsNavigationDestination.OpenSourceLicensesSettings)
    }

    override fun onScreenViewed() {
        analytics.settingsScreenViewed()
    }
}