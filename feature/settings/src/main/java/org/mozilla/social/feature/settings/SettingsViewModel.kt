package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val logout: Logout,
) : ViewModel() {

    fun onLogoutClicked() {
        logout()
    }
}