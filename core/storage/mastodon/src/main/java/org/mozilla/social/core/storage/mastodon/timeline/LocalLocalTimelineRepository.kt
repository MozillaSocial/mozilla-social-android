package org.mozilla.social.core.storage.mastodon.timeline

import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.model.Status

class LocalLocalTimelineRepository(private val dao: LocalTimelineStatusDao) {
    suspend fun deletePost(statusId: String) {
        dao.deletePost(statusId)
    }

    suspend fun insert(status: Status) {
        dao.insert(status.toLocalTimelineStatus())
    }

    suspend fun insertAll(statuses: List<Status>) {
        dao.insertAll(statuses.map { it.toLocalTimelineStatus() })
    }

    suspend fun deleteLocalTimeline() {
        dao.deleteLocalTimeline()
    }
}

private fun Status.toLocalTimelineStatus() = LocalTimelineStatus(
    statusId = statusId,
    accountId = account.accountId,
    pollId = poll?.pollId,
    boostedStatusId = boostedStatus?.statusId,
    boostedPollId = boostedStatus?.poll?.pollId,
    boostedStatusAccountId = boostedStatus?.account?.accountId,
)
