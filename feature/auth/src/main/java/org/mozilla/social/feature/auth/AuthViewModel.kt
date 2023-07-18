package org.mozilla.social.feature.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.datastore.UserPreferencesDatastore

 class AuthViewModel(
    userPreferencesDatastore: UserPreferencesDatastore,
    private val log: Log
) : ViewModel() {

     val isSignedIn: Flow<Boolean?> = userPreferencesDatastore.dataStore.data.map {
         !it.accessToken.isNullOrBlank()
     }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}