package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import social.firefly.core.accounts.AccountsManager

class GetDomain(
    private val accountsManager: AccountsManager,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<String> = accountsManager.getActiveAccountFlow().mapLatest { it.domain }
}