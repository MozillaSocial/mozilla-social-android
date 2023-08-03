package org.mozilla.social.core.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

class TimelineUseCase(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository
) {
    suspend fun getHomeTimeline(): List<Post> {
        return timelineRepository.getHomeTimeline().getInReplyToAccountNames()
    }

    private suspend fun List<Status>.getInReplyToAccountNames(): List<Post> =
        coroutineScope {
            map { status ->
                // get in reply to account names
                async {
                    Post(
                        status = status,
                        inReplyToAccountName = status.inReplyToAccountId?.let { accountId ->
                            accountRepository.getAccount(accountId).displayName
                        }
                    )
                }
            }.map {
                it.await()
            }
        }
}