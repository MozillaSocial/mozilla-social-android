package org.mozilla.social.core.storage.mastodon.timeline

import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.model.Status

class LocalFederatedTimelineRepository(private val dao: FederatedTimelineStatusDao) {
    suspend fun deletePost(statusId: String) {
        dao.deletePost(statusId)
    }

    suspend fun insert(status: Status) {
        dao.insert(status.toFederatedTimelineStatus())
    }

    suspend fun insertAll(statuses: List<Status>) {
        dao.insertAll(statuses.map { it.toFederatedTimelineStatus() })
    }

    suspend fun deleteFederatedTimeline() {
        dao.deleteFederatedTimeline()
    }
}

private fun Status.toFederatedTimelineStatus(): FederatedTimelineStatus = FederatedTimelineStatus(
    statusId = statusId,
    accountId = account.accountId,
    pollId = poll?.pollId,
    boostedStatusId = boostedStatus?.statusId,
    boostedPollId = boostedStatus?.poll?.pollId,
    boostedStatusAccountId = boostedStatus?.account?.accountId,
)