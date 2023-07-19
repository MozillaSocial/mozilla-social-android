package org.mozilla.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.mozilla.social.core.data.repository.AuthRepository
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.feature.auth.AuthViewModel
import java.net.URL

class MainViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun onUserCodeReceived(code: String) {
        val httpUrl: HttpUrl? = URL(
            code.replace("mozsoc://", "http://")
        ).toHttpUrlOrNull()
        val codeString = httpUrl?.queryParameter("code")!!

        viewModelScope.launch {
            onTokenReceived(
                authRepository.fetchOAuthToken(
                    domain = "mozilla.social",
                    clientId = "MoJ_c0aOfXE-8RllOBvAKIeHUpr3usA5u3vS5HJEJ0M",
                    clientSecret = "1rg6NsrXDuR81UM_ljyv_FNDj8TaInk6pbRok2eqmiM",
                    redirectUri = AuthViewModel.AUTH_SCHEME,
                    code = codeString,
                    grantType = "authorization_code",
                )
            )
        }
    }

    private fun onTokenReceived(accessToken: String) {
        viewModelScope.launch {
            userPreferencesDatastore.dataStore.updateData {
                it.toBuilder()
                    .setAccessToken(accessToken)
                    .build()
            }
        }
    }
}