package org.mozilla.social.core.storage.mastodon.timeline

import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.model.Status

class LocalHomeTimelineRepository(private val dao: HomeTimelineStatusDao) {
    suspend fun deletePost(statusId: String) {
        dao.deletePost(statusId)
    }

    suspend fun insert(status: Status) {
        dao.insert(status.toHomeTimelineStatus())
    }

    suspend fun insertAll(statuses: List<Status>) {
        dao.insertAll(statuses.map { it.toHomeTimelineStatus() })
    }

    suspend fun deleteHomeTimeline() {
        dao.deleteHomeTimeline()
    }
}

private fun Status.toHomeTimelineStatus() = HomeTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        pollId = poll?.pollId,
        boostedStatusId = boostedStatus?.statusId,
        boostedPollId = boostedStatus?.poll?.pollId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
    )
