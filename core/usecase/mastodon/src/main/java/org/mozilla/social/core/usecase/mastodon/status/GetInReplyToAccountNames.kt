package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.exceptions.GetAccountFailedException

class GetInReplyToAccountNames internal constructor(
    private val accountRepository: AccountRepository,
) {

    @Suppress("MagicNumber")
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
                    } catch (e: GetAccountFailedException) {
                        // if there was a 404, then the account doesn't exist.  Continue as if
                        // the status is not a reply
                        if (e.errorCode == 404) {
                            return@async status
                        }
                        throw e
                    }
                }
            }.map {
                it.await()
            }
        }
}