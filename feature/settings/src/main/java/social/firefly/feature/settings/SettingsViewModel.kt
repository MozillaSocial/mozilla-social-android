package social.firefly.feature.settings

import androidx.lifecycle.ViewModel
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo

class SettingsViewModel(
    private val analytics: SettingsAnalytics,
    private val navigateTo: NavigateTo,
) : ViewModel(), social.firefly.feature.settings.SettingsInteractions {

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