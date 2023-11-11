package org.mozilla.social

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.domain.Login
import org.mozilla.social.core.navigation.AuthNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

/**
 * Main view model- handles login logic and logout navigation
 */
class MainViewModel(
    private val login: Login,
    private val navigateTo: NavigateTo,
    private val isSignedInFlow: IsSignedInFlow,
    private val userPreferencesDatastore: UserPreferencesDatastore,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.Main) {
            isSignedInFlow().collectLatest {
                if (!it) {
                    navigateTo(NavigationDestination.Auth)
                } else {
                    navigateTo(NavigationDestination.Tabs)
                }
            }
        }
    }

    fun onNewIntentReceived(intent: Intent) {
        // Attempt to resolve the intent as a login event
        viewModelScope.launch { login.onNewIntentReceived(intent) }
    }

    suspend fun preloadData() {
        userPreferencesDatastore.preloadData()
    }
}