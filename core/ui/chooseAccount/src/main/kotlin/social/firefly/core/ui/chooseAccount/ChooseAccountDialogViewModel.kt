package social.firefly.core.ui.chooseAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.usecase.mastodon.auth.SwitchActiveAccount

class ChooseAccountDialogViewModel(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    eventRelay: EventRelay,
    private val navigateTo: NavigateTo,
    private val switchActiveAccount: SwitchActiveAccount,
) : ViewModel(), ChooseAccountInteractions {

    val accounts = userPreferencesDatastoreManager.dataStores.map { dataStores ->
        dataStores.map { dataStore ->
            ChooseAccountUiState(
                accountId = dataStore.accountId,
                userName = dataStore.userName,
                domain = dataStore.domain,
                avatarUrl = dataStore.avatarUrl,
            )
        }
    }

    private val _isOpen = MutableStateFlow(false)
    val isOpen = _isOpen.asStateFlow()

    init {
        viewModelScope.launch {
            eventRelay.navigationEvents.collect { event ->
                when (event) {
                    is Event.ChooseAccountForSharing -> {
                        if (userPreferencesDatastoreManager.isLoggedInToMultipleAccounts) {
                            _isOpen.update { true }
                        } else {
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
                    else -> {}
                }
            }
        }
    }

    override fun onAccountClicked(
        accountId: String,
        domain: String,
    ) {
        _isOpen.update { false }
        viewModelScope.launch {
            switchActiveAccount(accountId, domain)
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

    override fun onDismissRequest() {
        _isOpen.update { false }
    }
}