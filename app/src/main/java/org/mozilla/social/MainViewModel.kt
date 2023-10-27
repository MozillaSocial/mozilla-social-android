package org.mozilla.social

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.domain.Login
import org.mozilla.social.core.domain.NavigationEvent
import org.mozilla.social.core.domain.NavigationRelay

/**
 * Main view model- handles login logic and logout navigation
 */
class MainViewModel(
    private val login: Login,
    private val navigationRelay: NavigationRelay,
    private val isSignedInFlow: IsSignedInFlow,
) : ViewModel() {
    val navigationEvents: SharedFlow<NavigationEvent> = navigationRelay.navigationEvents

    init {
        viewModelScope.launch {
            isSignedInFlow().collectLatest {
                if (!it) {
                    navigationRelay.navigateToLogin()
                }
            }
        }
    }

    fun onNewIntentReceived(intent: Intent) {
        // Attempt to resolve the intent as a login event
        viewModelScope.launch { login.onNewIntentReceived(intent) }
    }
}