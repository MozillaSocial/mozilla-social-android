package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.datastore.UserPreferencesDatastore

class SettingsViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val log: Log,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    val isSignedIn = userPreferencesDatastore.dataStore.data.map {
        !it.accessToken.isNullOrBlank()
    }.distinctUntilChanged()

    fun toggleSwitch() {
        _isToggled.value = _isToggled.value.not()
    }

    fun logoutUser() {
        viewModelScope.launch {
            userPreferencesDatastore.dataStore.updateData {
                it.toBuilder()
                    .setAccessToken("")
                    .build()
            }
        }
    }
}