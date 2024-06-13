package social.firefly.splash

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import social.firefly.IntentHandler
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.auth.UpdateAllLoggedInAccounts
import social.firefly.ui.AppState

class SplashViewModel(
    private val navigateTo: NavigateTo,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val timelineRepository: TimelineRepository,
    private val updateAllLoggedInAccounts: UpdateAllLoggedInAccounts,
    private val intentHandler: IntentHandler,
) : ViewModel() {

    fun initialize(intent: Intent?) {
        viewModelScope.launch {
            // Do any cleanup necessary before navigating away from the splash screen
            timelineRepository.deleteHomeTimeline()

            AppState.navigationCollectionCompletable.await()

            if (userPreferencesDatastoreManager.isLoggedInToAtLeastOneAccount) {
                navigateTo(NavigationDestination.Tabs)
                intent?.let { intentHandler.handleIntent(intent) }
                updateAllLoggedInAccounts()
            } else {
                navigateTo(NavigationDestination.Auth)
            }
        }
    }
}