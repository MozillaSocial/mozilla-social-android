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
import social.firefly.core.share.ShareInfo
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

    fun initialize(intent: Intent) {
        viewModelScope.launch {
            // We restore the user's place in their timeline by removing items in the database
            // above their last seen item.  This needs to happen before we start observing the
            // home timeline.
            //TODO maybe restore this if we want to restore a user's place in their timeline
            // I ran into bugs so disabling for now
//            val lastSeenId = CompletableDeferred<String>()
//            launch {
//                userPreferencesDatastore.lastSeenHomeStatusId.collectLatest {
//                    lastSeenId.complete(it)
//                    cancel()
//                }
//            }
//            timelineRepository.deleteHomeStatusesBeforeId(lastSeenId.await())
            //TODO delete this line if you uncomment the above lines
            timelineRepository.deleteHomeTimeline()

            AppState.navigationCollectionCompletable.await()
            launch(Dispatchers.Main) {
                isSignedInFlow().collectLatest {
                    if (!it) {
                        navigateTo(NavigationDestination.Auth)
                    } else {
                        navigateTo(NavigationDestination.Tabs)
                        handleIntent(intent)
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

    fun handleIntent(intent: Intent) {
        when {
            intent.action == Intent.ACTION_SEND -> {
                when {
                    intent.type == "text/plain" -> {
                        handleSendTextIntentReceived(intent)
                    }
                }
            }
        }
    }

    private fun handleSendTextIntentReceived(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
            ShareInfo.sharedText = sharedText
            navigateTo(NavigationDestination.NewPost())
        }
    }
}
