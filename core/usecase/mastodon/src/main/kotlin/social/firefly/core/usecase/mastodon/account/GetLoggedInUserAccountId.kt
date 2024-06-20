package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.runBlocking
import social.firefly.core.accounts.AccountsManager

/**
 * Synchronously gets the account ID of the current logged in user
 */
class GetLoggedInUserAccountId(
    private val accountsManager: AccountsManager,
) {
    operator fun invoke(): String =
        runBlocking {
            accountsManager.getActiveAccount().accountId
        }
}
