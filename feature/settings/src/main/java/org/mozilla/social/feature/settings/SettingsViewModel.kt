package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.datastore.UserPreferencesDatastore

class SettingsViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val log: Log,
    private val onLogout: () -> Unit,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesDatastore.dataStore.data.map {
                !it.accessToken.isNullOrBlank()
            }.map { signedIn ->
                if (!signedIn) {
                    onLogout()
                }
            }.collect()
        }
    }

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