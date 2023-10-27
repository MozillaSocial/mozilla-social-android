package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val logout: Logout,
) : ViewModel() {

    fun onLogoutClicked() {
        viewModelScope.launch {
            logout()
        }
    }
}