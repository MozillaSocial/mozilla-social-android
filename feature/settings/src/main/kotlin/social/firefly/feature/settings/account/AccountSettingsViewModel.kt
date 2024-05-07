package social.firefly.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import social.firefly.common.loadResource
import social.firefly.common.utils.StringFactory
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.model.Account
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.usecase.mastodon.account.GetDomain
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.auth.Logout
import social.firefly.core.usecase.mastodon.auth.UpdateAllLoggedInAccounts
import social.firefly.feature.settings.R
import timber.log.Timber

class AccountSettingsViewModel(
    private val logout: Logout,
    private val openLink: OpenLink,
    private val analytics: SettingsAnalytics,
    getDomain: GetDomain,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
    userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    updateAllLoggedInAccounts: UpdateAllLoggedInAccounts,
) : ViewModel(), AccountSettingsInteractions {

//    private val userAccountId: String = getLoggedInUserAccountId()
//    private val domain = getDomain()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue = null,
//        )
//
//    val subtitle: StateFlow<StringFactory?> = domain.map { domain ->
//        domain?.let { StringFactory.resource(R.string.manage_your_account, it) }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = null,
//    )
//
//    val userHeader = loadResource {
//        accountRepository.getAccount(userAccountId).toUserHeader()
//    }

    val otherAccounts = userPreferencesDatastoreManager.dataStores.combine(
        userPreferencesDatastoreManager.activeUserDatastore
    ) { dataStores, activeDataStore ->
        dataStores.filterNot {
            it == activeDataStore
        }.map { dataStore ->
            LoggedInAccount(
                accountId = dataStore.accountId.first(),
                userName = dataStore.userName.first(),
                domain = dataStore.domain.first(),
                avatarUrl = dataStore.avatarUrl.first(),
            )
        }
    }

    val activeAccount = userPreferencesDatastoreManager.activeUserDatastore.map { dataStore ->
        LoggedInAccount(
            accountId = dataStore.accountId.first(),
            userName = dataStore.userName.first(),
            domain = dataStore.domain.first(),
            avatarUrl = dataStore.avatarUrl.first(),
        )
    }

    init {
        viewModelScope.launch {
            try {
                updateAllLoggedInAccounts()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onLogoutClicked() {
        analytics.logoutClicked()
        viewModelScope.launch {
            logout()
        }
    }

    override fun onManageAccountClicked(domain: String) {
        openLink("$domain/settings/profile")
    }


    override fun onScreenViewed() {
        analytics.accountSettingsViewed()
    }
}

