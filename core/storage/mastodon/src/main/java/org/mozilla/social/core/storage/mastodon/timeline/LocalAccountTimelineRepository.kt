package org.mozilla.social.core.storage.mastodon.timeline

import androidx.paging.PagingSource
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.model.Status

class LocalAccountTimelineRepository(private val dao: AccountTimelineStatusDao) {
    suspend fun deletePost(statusId: String) {
        dao.deletePost(statusId)
    }

    suspend fun deleteAccountTimeline(accountId: String) {
        dao.deleteAccountTimeline(accountId)
    }

    suspend fun insertAll(statuses: List<Status>) {
        dao.insertAll(statuses.map { it.toAccountTimelineStatus() })
    }

    fun accountTimelinePagingSource(
        accountId: String,
    ): PagingSource<Int, AccountTimelineStatusWrapper> {
        return dao.accountTimelinePagingSource(accountId)
    }
}

fun Status.toAccountTimelineStatus() = AccountTimelineStatus(
    statusId = statusId,
    accountId = account.accountId,
    pollId = poll?.pollId,
    boostedStatusId = boostedStatus?.statusId,
    boostedStatusAccountId = boostedStatus?.account?.accountId,
    boostedPollId = boostedStatus?.poll?.pollId,
)