package org.mozilla.social.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.datastore.UserPreferencesDatastore

class SettingsViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val log: Log,
) : ViewModel() {

    private val _isToggled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isToggled = _isToggled.asStateFlow()

    val isSignedIn = userPreferencesDatastore.dataStore.data.map {
        it.accessToken != null
    }

    fun toggleSwitch() {
        _isToggled.value = _isToggled.value.not()
    }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}