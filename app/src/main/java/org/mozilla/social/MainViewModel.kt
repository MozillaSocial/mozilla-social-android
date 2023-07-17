package org.mozilla.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import java.net.URL

class MainViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
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
        }
    }
}