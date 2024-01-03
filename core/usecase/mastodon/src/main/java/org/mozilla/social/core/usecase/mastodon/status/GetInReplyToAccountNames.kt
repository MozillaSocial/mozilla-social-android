package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.exceptions.AccountNotFoundException
import timber.log.Timber

class GetInReplyToAccountNames internal constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(
        statuses: List<Status>
    ): List<Status> =
        coroutineScope {
            statuses.map { status ->
                async {
                    try {
                        status.copy(
                            inReplyToAccountName = status.inReplyToAccountId?.let { accountId ->
                                accountRepository.getAccount(accountId).displayName
                            },
                        )
                    } catch (e: AccountNotFoundException) {
                        Timber.e(e)
                        return@async status
                    }
                }
            }.map {
                it.await()
            }
        }
}