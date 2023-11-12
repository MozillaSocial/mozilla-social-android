package org.mozilla.social.core.storage.mastodon.timeline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.storage.mastodon.DatabaseDelegate
import org.mozilla.social.core.storage.mastodon.LocalStatusRepository

/**
 * Local timeline repository- local as in local storage- NOT the local timeline
 */
class LocalTimelineRepository(
    private val homeTimelineStatusDao: HomeTimelineStatusDao,
    private val localTimelineStatusDao: LocalTimelineStatusDao,
    private val federatedTimelineStatusDao: FederatedTimelineStatusDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val localStatusRepository: LocalStatusRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val homeTimelineRepository: LocalHomeTimelineRepository,
    private val localLocalTimelineRepository: LocalLocalTimelineRepository,
    private val localFederatedTimelineRepository: LocalFederatedTimelineRepository,
    private val localHashtagTimelineRepository: LocalHashtagTimelineRepository,
    private val localAccountTimelineRepository: LocalAccountTimelineRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun insertStatusIntoTimelines(status: Status) = withContext(ioDispatcher) {

        homeTimelineStatusDao.insert(
            HomeTimelineStatus(
                statusId = status.statusId,
                accountId = status.account.accountId,
                pollId = status.poll?.pollId,
                boostedStatusId = status.boostedStatus?.statusId,
                boostedPollId = status.boostedStatus?.poll?.pollId,
                boostedStatusAccountId = status.boostedStatus?.account?.accountId,
            )
        )

        if (status.visibility == StatusVisibility.Public) {
            localTimelineStatusDao.insert(
                LocalTimelineStatus(
                    statusId = status.statusId,
                    accountId = status.account.accountId,
                    pollId = status.poll?.pollId,
                    boostedStatusId = status.boostedStatus?.statusId,
                    boostedPollId = status.boostedStatus?.poll?.pollId,
                    boostedStatusAccountId = status.boostedStatus?.account?.accountId,
                )
            )
            federatedTimelineStatusDao.insert(
                FederatedTimelineStatus(
                    statusId = status.statusId,
                    accountId = status.account.accountId,
                    pollId = status.poll?.pollId,
                    boostedStatusId = status.boostedStatus?.statusId,
                    boostedPollId = status.boostedStatus?.poll?.pollId,
                    boostedStatusAccountId = status.boostedStatus?.account?.accountId,
                )
            )
        }
    }

    suspend fun deleteFromTimelines(
        statusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            localStatusRepository.updateIsBeingDeleted(statusId, true)
            statusRepository.deleteStatus(statusId)
            databaseDelegate.withTransaction {
                homeTimelineRepository.deletePost(statusId)
                localLocalTimelineRepository.deletePost(statusId)
                localFederatedTimelineRepository.deletePost(statusId)
                localHashtagTimelineRepository.deletePost(statusId)
                localAccountTimelineRepository.deletePost(statusId)
                localStatusRepository.deleteStatus(statusId)
            }
        } catch (e: Exception) {
            localStatusRepository.updateIsBeingDeleted(statusId, false)
            throw DeleteStatusFailedException(e)
        }
    }.await()
}

class DeleteStatusFailedException(e: Exception) : Exception(e)
