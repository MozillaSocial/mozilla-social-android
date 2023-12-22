package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.AccountRepository

class GetInReplyToAccountNames internal constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(
        statuses: List<Status>
    ): List<Status> =
        coroutineScope {
            statuses.map { status ->
                async {
                    status.copy(
                        inReplyToAccountName = status.inReplyToAccountId?.let { accountId ->
                            accountRepository.getAccount(accountId).displayName
                        },
                    )
                }
            }.map {
                it.await()
            }
        }
}