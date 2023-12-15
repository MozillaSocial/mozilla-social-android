package org.mozilla.social.core.repository.paging

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.AccountRepository

suspend fun List<Status>.getInReplyToAccountNames(accountRepository: AccountRepository): List<Status> =
    coroutineScope {
        map { status ->
            // get in reply to account names
            async {
                status.copy(
                    inReplyToAccountName =
                        status.inReplyToAccountId?.let { accountId ->
                            accountRepository.getAccount(accountId).displayName
                        },
                )
            }
        }.map {
            it.await()
        }
    }
