package social.firefly

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.TimelineRepository
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
    private val timelineRepository: TimelineRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            // We restore the user's place in their timeline by removing items in the database
            // above their last seen item.  This needs to happen before we start observing the
            // home timeline.
            val lastSeenId = CompletableDeferred<String>()
            launch {
                userPreferencesDatastore.lastSeenHomeStatusId.collectLatest {
                    lastSeenId.complete(it)
                    cancel()
                }
            }
            timelineRepository.deleteHomeStatusesBeforeId(lastSeenId.await())

            AppState.navigationCollectionCompletable.await()
            launch(Dispatchers.Main) {
                isSignedInFlow().collectLatest {
                    if (!it) {
                        navigateTo(NavigationDestination.Auth)
                    } else {
                        navigateTo(NavigationDestination.Tabs)
                    }
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
