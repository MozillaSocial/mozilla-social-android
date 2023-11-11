package org.mozilla.social.feed.remoteMediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatusWrapper
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatusWrapper
import org.mozilla.social.core.domain.remotemediators.getInReplyToAccountNames
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class LocalTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val statusRepository: StatusRepository,
    private val socialDatabase: SocialDatabase,
) : RemoteMediator<Int, LocalTimelineStatusWrapper>() {

    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalTimelineStatusWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    pageSize = state.config.initialLoadSize
                    timelineRepository.getPublicTimeline(
                        localOnly = true,
                        olderThanId = null,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getPublicTimeline(
                        localOnly = true,
                        olderThanId = null,
                        immediatelyNewerThanId = firstItem.status.statusId,
                        loadSize = pageSize,
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getPublicTimeline(
                        localOnly = true,
                        olderThanId = lastItem.status.statusId,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }
            }

            val result = response.statuses.getInReplyToAccountNames(accountRepository)

            socialDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    socialDatabase.localTimelineDao().deleteLocalTimeline()
                }

                statusRepository.saveStatusesToDatabase(result)

                socialDatabase.localTimelineDao().insertAll(result.map {
                    LocalTimelineStatus(
                        statusId = it.statusId,
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
                endOfPaginationReached = when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                    LoadType.REFRESH,
                    LoadType.APPEND -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }
}
