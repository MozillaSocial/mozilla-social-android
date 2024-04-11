package social.firefly

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.usecase.mastodon.auth.IsSignedInFlow
import social.firefly.core.usecase.mastodon.auth.Login
import social.firefly.ui.AppState

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
            AppState.navigationCollectionCompletable.await()
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
