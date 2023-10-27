package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val analytics: Analytics,
    private val logout: Logout,
    private val isSignedInFlow: IsSignedInFlow,
    private val log: Log,
    private val onLogout: () -> Unit,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    private val _isAnalyticsToggledOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAnalyticsToggledOn = _isAnalyticsToggledOn.asStateFlow()

    init {
        viewModelScope.launch {
            appPreferencesDatastore.trackAnalytics.collectLatest { toggled ->
                _isAnalyticsToggledOn.value = toggled
            }
            isSignedInFlow().collectLatest { isSignedIn ->
                if (isSignedIn) {
                    onLogout()
                }
            }
        }
    }

    fun toggleAnalytics() {
        _isAnalyticsToggledOn.value = _isAnalyticsToggledOn.value.not()
        viewModelScope.launch { saveSettingsChanges() }
    }

    private suspend fun saveSettingsChanges() {
        appPreferencesDatastore.toggleTrackAnalytics(_isAnalyticsToggledOn.value)
        analytics.toggleAnalyticsTracking(_isAnalyticsToggledOn.value)
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