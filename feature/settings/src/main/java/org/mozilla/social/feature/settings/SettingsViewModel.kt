package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class SettingsViewModel(
    private val navigateTo: NavigateTo,
) : ViewModel() {

    fun onAboutClicked() {
        navigateTo(SettingsNavigationDestination.AboutSettings)
    }

    fun onAccountClicked() {
        navigateTo(SettingsNavigationDestination.AccountSettings)
    }

    fun onPrivacyClicked() {
        navigateTo(SettingsNavigationDestination.PrivacySettings)
    }
}
