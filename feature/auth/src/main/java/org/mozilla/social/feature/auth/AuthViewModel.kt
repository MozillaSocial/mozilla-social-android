package org.mozilla.social.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import java.net.URL

 class AuthViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val log: Log,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Default)
    val uiState: StateFlow<UiState> = _uiState

     val isSignedIn = userPreferencesDatastore.dataStore.data.map { it.accessToken != null }

    fun onTokenReceived(authUri: String) {
        val httpUrl: HttpUrl? = URL(
            authUri.replace("mozsoc://", "http://")
        ).toHttpUrlOrNull()
        val accessToken = httpUrl?.queryParameter("code")

        viewModelScope.launch {
            userPreferencesDatastore.dataStore.updateData {
                it.toBuilder()
                    .setAccessToken(accessToken)
                    .build()
            }
            _uiState.update { UiState.SignedIn }
        }
    }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}