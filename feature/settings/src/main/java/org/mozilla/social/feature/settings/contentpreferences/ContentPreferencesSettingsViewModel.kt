package org.mozilla.social.feature.settings.contentpreferences

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class ContentPreferencesSettingsViewModel(
    private val navigateTo: NavigateTo,
) : ViewModel() {
    fun onMutedUsersClicked() {
        navigateTo(SettingsNavigationDestination.MutedUsersSettings)
    }

    fun onBlockedUsersClicked() {
        navigateTo(SettingsNavigationDestination.BlockedUsersSettings)
    }
}
