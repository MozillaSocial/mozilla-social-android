package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

class LogoutOfAllAccounts(
    private val databaseDelegate: DatabaseDelegate,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
    private val navigateTo: NavigateTo,
    private val accountsManager: AccountsManager,
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke() =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            navigateTo(NavigationDestination.Auth)
            accountsManager.deleteAllAccounts()
            databaseDelegate.clearAllTables()
        }
}