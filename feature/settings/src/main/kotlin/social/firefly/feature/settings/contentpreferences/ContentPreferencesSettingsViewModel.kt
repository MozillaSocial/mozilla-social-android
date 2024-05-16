package social.firefly.feature.settings.contentpreferences

import androidx.lifecycle.ViewModel
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo

class ContentPreferencesSettingsViewModel(
    private val navigateTo: NavigateTo,
    private val analytics: SettingsAnalytics,
) : ViewModel(), ContentPreferencesSettingsInteractions {
    override fun onMutedUsersClicked() {
        navigateTo(SettingsNavigationDestination.MutedUsersSettings)
    }

    override fun onBlockedUsersClicked() {
        navigateTo(SettingsNavigationDestination.BlockedUsersSettings)
    }

    override fun onBlockedDomainsClicked() {
        navigateTo(SettingsNavigationDestination.BlockedDomains)
    }

    override fun onScreenViewed() {
        analytics.contentPreferencesSettingsViewed()
    }
}
