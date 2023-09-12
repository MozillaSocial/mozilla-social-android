package org.mozilla.social

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.Login

class MainViewModel(
    private val login: Login,
) : ViewModel() {

    fun onNewIntentReceived(intent: Intent) {
        // Attempt to resolve the intent as a login event
        viewModelScope.launch { login.onNewIntentReceived(intent) }
    }
}
