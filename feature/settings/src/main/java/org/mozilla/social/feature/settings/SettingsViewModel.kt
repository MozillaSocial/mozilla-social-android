package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val logout: Logout,
    private val isSignedInFlow: IsSignedInFlow,
    private val log: Log,
    private val onLogout: () -> Unit,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    init {
        viewModelScope.launch {
            isSignedInFlow().collectLatest { isSignedIn ->
                if (isSignedIn) {
                    onLogout()
                }
            }
        }
    }

    fun toggleSwitch() {
        _isToggled.value = _isToggled.value.not()
    }

    fun logoutUser() {
        viewModelScope.launch {
            logout()
        }
    }
}