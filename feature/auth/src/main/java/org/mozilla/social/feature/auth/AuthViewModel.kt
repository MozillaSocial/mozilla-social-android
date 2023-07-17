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

     val isSignedIn = userPreferencesDatastore.dataStore.data.map {
         it.accessToken != null
     }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}