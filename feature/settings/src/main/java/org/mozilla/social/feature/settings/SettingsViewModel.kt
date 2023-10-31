package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val analytics: Analytics,
    private val logout: Logout,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    private val _isAnalyticsToggledOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAnalyticsToggledOn = _isAnalyticsToggledOn.asStateFlow()

    init {
        GlobalScope.launch {
            appPreferencesDatastore.trackAnalytics.collectLatest { enabled ->
                saveSettingsChanges(enabled)
            }
        }
    }

    private suspend fun saveSettingsChanges(optToggle: Boolean) {
        appPreferencesDatastore.toggleTrackAnalytics(optToggle)
        analytics.toggleAnalyticsTracking(optToggle)
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