package org.mozilla.social.core.domain.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.delay
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatusWrapper

@OptIn(ExperimentalPagingApi::class)
class HashTagTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val statusRepository: StatusRepository,
    private val socialDatabase: SocialDatabase,
    private val hashTag: String,
) : RemoteMediator<Int, HashTagTimelineStatusWrapper>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HashTagTimelineStatusWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    pageSize = state.config.initialLoadSize
                    timelineRepository.getHashtagTimeline(
                        hashTag = hashTag,
                        olderThanId = null,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHashtagTimeline(
                        hashTag = hashTag,
                        olderThanId = null,
                        immediatelyNewerThanId = firstItem.status.statusId,
                        loadSize = pageSize,
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHashtagTimeline(
                        hashTag = hashTag,
                        olderThanId = lastItem.status.statusId,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }
            }.getInReplyToAccountNames(accountRepository)

            socialDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    socialDatabase.hashTagTimelineDao().deleteHashTagTimeline(hashTag)
                }

                statusRepository.saveStatusesToDatabase(response)

                socialDatabase.hashTagTimelineDao().insertAll(response.map {
                    HashTagTimelineStatus(
                        statusId = it.statusId,
                        hashTag = hashTag,
                        createdAt = it.createdAt,
                        accountId = it.account.accountId,
                        pollId = it.poll?.pollId,
                        boostedStatusId = it.boostedStatus?.statusId,
                        boostedStatusAccountId = it.boostedStatus?.account?.accountId,
                        boostedPollId = it.boostedStatus?.poll?.pollId,
                    )
                })
            }

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(200)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.size != pageSize
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}