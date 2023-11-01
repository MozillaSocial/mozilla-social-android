package org.mozilla.social.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.Login

class AuthViewModel(private val login: Login) : ViewModel() {

    val defaultUrl: StateFlow<String> = MutableStateFlow(if (BuildConfig.DEBUG) BuildConfig.stagingUrl else prod)

    fun onLoginClicked(context: Context, domain: String) {
        viewModelScope.launch {
            login(context, domain)
        }
    }

    companion object {
        private const val prod = "mozilla.social"
    }
}