package org.mozilla.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.feature.auth.UiState
import java.net.URL

class MainViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
                    private val log: Log,
): ViewModel() {

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
//            _uiState .update { UiState.SignedIn }
        }
    }
}