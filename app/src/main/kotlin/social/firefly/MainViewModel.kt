package social.firefly

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.AppPreferences
import social.firefly.core.datastore.AppPreferencesDatastore
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.share.ShareInfo
import social.firefly.core.usecase.mastodon.auth.IsSignedInFlow
import social.firefly.ui.AppState

class MainViewModel(
    private val navigateTo: NavigateTo,
    private val isSignedInFlow: IsSignedInFlow,
    private val timelineRepository: TimelineRepository,
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
            isSignedInFlow().collectLatest {
                launch(Dispatchers.Main) {
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

    fun handleIntent(intent: Intent) {
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
            navigateTo(
                NavigationDestination.NewPost(
                    navOptions = navOptions {
                        popUpTo(
                            NavigationDestination.Tabs.route
                        )
                    }
                )
            )
        }
    }

    private fun handleSendImageIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedImageUri = sharedUri
            navigateTo(
                NavigationDestination.NewPost(
                    navOptions = navOptions {
                        popUpTo(
                            NavigationDestination.Tabs.route
                        )
                    }
                )
            )
        }
    }

    private fun handleSendVideoIntentReceived(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { sharedUri ->
            ShareInfo.sharedVideoUri = sharedUri
            navigateTo(
                NavigationDestination.NewPost(
                    navOptions = navOptions {
                        popUpTo(
                            NavigationDestination.Tabs.route
                        )
                    }
                )
            )
        }
    }
}
