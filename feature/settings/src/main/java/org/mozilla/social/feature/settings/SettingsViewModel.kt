package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.domain.Logout

class SettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val logout: Logout,
) : ViewModel() {
    private val _isAnalyticsToggledOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAnalyticsToggledOn = _isAnalyticsToggledOn.asStateFlow()

    init {
        viewModelScope.launch {
            appPreferencesDatastore.trackAnalytics.collectLatest { enabled ->
                _isAnalyticsToggledOn.value = enabled
            }
        }
    }

    fun toggleAnalytics() {
        viewModelScope.launch {
            saveSettingsChanges(_isAnalyticsToggledOn.value.not())
        }
    }

    private suspend fun saveSettingsChanges(optToggle: Boolean) {
        appPreferencesDatastore.toggleTrackAnalytics(optToggle)
    }

    fun logoutUser() {
        viewModelScope.launch {
            logout()
        }
    }
}