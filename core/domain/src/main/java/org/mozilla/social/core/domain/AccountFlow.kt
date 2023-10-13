package org.mozilla.social.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.model.Account

class AccountFlow(
    private val accountIdFlow: AccountIdFlow,
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(): Flow<Account> =
        accountIdFlow().transformLatest { emit(accountRepository.getAccount(it)) }
}