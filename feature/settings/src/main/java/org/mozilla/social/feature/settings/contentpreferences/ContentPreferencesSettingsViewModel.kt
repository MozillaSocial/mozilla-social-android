package org.mozilla.social.feature.settings.contentpreferences

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.analytics.SettingsAnalytics

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
