package org.mozilla.social.feature.settings.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.core.datastore.AppPreferencesDatastore

class PrivacySettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
) : ViewModel() {

    val allowAnalytics =
        appPreferencesDatastore.allowAnalytics.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun toggleAllowAnalytics() {
        viewModelScope.launch {
            appPreferencesDatastore.allowAnalytics(allowAnalytics.value.not())
        }
    }
}