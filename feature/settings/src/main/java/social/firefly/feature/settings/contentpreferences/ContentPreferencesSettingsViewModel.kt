package social.firefly.feature.settings.contentpreferences

import androidx.lifecycle.ViewModel
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.analytics.SettingsAnalytics

class ContentPreferencesSettingsViewModel(
    private val navigateTo: NavigateTo,
    private val analytics: SettingsAnalytics,
) : ViewModel(), ContentPreferencesSettingsInteractions {
    fun onMutedUsersClicked() {
        navigateTo(SettingsNavigationDestination.MutedUsersSettings)
    }

    fun onBlockedUsersClicked() {
        navigateTo(SettingsNavigationDestination.BlockedUsersSettings)
    }

    override fun onScreenViewed() {
        analytics.contentPreferencesSettingsViewed()
    }
}
