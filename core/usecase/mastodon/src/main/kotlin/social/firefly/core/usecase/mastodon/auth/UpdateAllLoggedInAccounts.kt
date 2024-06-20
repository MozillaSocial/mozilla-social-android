package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.accounts.model.MastodonAccount
import social.firefly.core.model.Account
import social.firefly.core.repository.mastodon.VerificationRepository
import timber.log.Timber

class UpdateAllLoggedInAccounts(
    private val verificationRepository: VerificationRepository,
    private val accountsManager: AccountsManager,
) {

    suspend operator fun invoke() = coroutineScope {
        accountsManager.getAllAccounts().map { account ->
            async {
                val accessToken = account.accessToken
                val domain = account.domain
                try {
                    AccountWrapper(
                        mastodonAccount = account,
                        account = verificationRepository.verifyUserCredentials(
                            accessToken,
                            domain,
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    null
                }
            }
        }.mapNotNull {
            it.await()
        }.forEach { accountWrapper ->
            accountsManager.updateAccountInfo(
                mastodonAccount = accountWrapper.mastodonAccount,
                avatarUrl = accountWrapper.account.avatarUrl,
                username = accountWrapper.account.username,
                defaultLanguage = accountWrapper.account.source?.defaultLanguage ?: ""
            )
        }
    }

    data class AccountWrapper(
        val mastodonAccount: MastodonAccount,
        val account: Account,
    )
}