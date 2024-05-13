package social.firefly

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.AppPreferences
import social.firefly.core.datastore.AppPreferencesDatastore
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.share.ShareInfo
import social.firefly.ui.AppState

class MainViewModel(
    private val navigateTo: NavigateTo,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val timelineRepository: TimelineRepository,
    private val eventRelay: EventRelay,
    appPreferencesDatastore: AppPreferencesDatastore,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val themeOption = appPreferencesDatastore.themeType.mapLatest {
        when (it) {
            AppPreferences.ThemeType.LIGHT -> ThemeOption.LIGHT
            AppPreferences.ThemeType.DARK -> ThemeOption.DARK
            else -> ThemeOption.SYSTEM
        }
    }

    fun initialize(intent: Intent) {
        viewModelScope.launch {
            // Do any cleanup necessary before navigating away from the splash screen
            timelineRepository.deleteHomeTimeline()

            AppState.navigationCollectionCompletable.await()

            if (userPreferencesDatastoreManager.isLoggedInToAtLeastOneAccount) {
                navigateTo(NavigationDestination.Tabs)
                handleIntent(intent)
            } else {
                navigateTo(NavigationDestination.Auth)
            }
        }
    }

    fun handleIntent(intent: Intent) {
        if (!userPreferencesDatastoreManager.isLoggedInToAtLeastOneAccount) return
        when {
            intent.action == Intent.ACTION_SEND -> {
                when {
                    intent.type == "text/plain" -> {
                        handleSendTextIntentReceived(intent)
                    }
                    intent.type?.contains("image") == true -> {
                        handleSendImageIntentReceived(intent)
                    }
                    intent.type?.contains("video") == true -> {
                        handleSendVideoIntentReceived(intent)
                    }
                }
            }
        }
    }

    private fun handleSendTextIntentReceived(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
            ShareInfo.sharedText = sharedText
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }

    private fun handleSendImageIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedImageUri = sharedUri
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }

    private fun handleSendVideoIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedVideoUri = sharedUri
            eventRelay.emitEvent(Event.ChooseAccountForSharing)
        }
    }
}
