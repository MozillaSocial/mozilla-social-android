package org.mozilla.social.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import java.net.URL

internal class AuthViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.Default)
    val uiState: StateFlow<UiState> = _uiState

    fun onTokenReceived(authUri: String) {
        val httpUrl: HttpUrl? = URL(
            authUri.replace("mozsoc://", "http://")
        ).toHttpUrlOrNull()
        val accessToken = httpUrl?.queryParameter("access_token")

        viewModelScope.launch {
            userPreferencesDatastore.dataStore.updateData {
                it.toBuilder()
                    .setAccessToken(accessToken)
                    .build()
            }
        }
    }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}