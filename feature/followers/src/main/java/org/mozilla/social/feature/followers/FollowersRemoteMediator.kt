package org.mozilla.social.feature.followers

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.usecase.mastodon.remotemediators.getInReplyToAccountNames

class FollowersRemoteMediator()

//) : RemoteMediator<Int, HashTagTimelineStatusWrapper>() {
//
//    @Suppress("ReturnCount")
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, HashTagTimelineStatusWrapper>
//    ): RemoteMediator.MediatorResult {
//        return try {
//            var pageSize: Int = state.config.pageSize
//            val response = when (loadType) {
//                LoadType.REFRESH -> {
//                    pageSize = state.config.initialLoadSize
//                    timelineRepository.getHashtagTimeline(
//                        hashTag = hashTag,
//                        olderThanId = null,
//                        immediatelyNewerThanId = null,
//                        loadSize = pageSize,
//                    )
//                }
//
//                LoadType.PREPEND -> {
//                    val firstItem = state.firstItemOrNull()
//                        ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
//                    timelineRepository.getHashtagTimeline(
//                        hashTag = hashTag,
//                        olderThanId = null,
//                        immediatelyNewerThanId = firstItem.status.statusId,
//                        loadSize = pageSize,
//                    )
//                }
//
//                LoadType.APPEND -> {
//                    val lastItem = state.lastItemOrNull()
//                        ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
//                    timelineRepository.getHashtagTimeline(
//                        hashTag = hashTag,
//                        olderThanId = lastItem.status.statusId,
//                        immediatelyNewerThanId = null,
//                        loadSize = pageSize,
//                    )
//                }
//            }
//
//            val result = response.statuses.getInReplyToAccountNames(accountRepository)
//
//            socialDatabase.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    socialDatabase.hashTagTimelineDao().deleteHashTagTimeline(hashTag)
//                }
//
//                saveStatusToDatabase(result)
//
//                socialDatabase.hashTagTimelineDao().insertAll(result.map {
//                    HashTagTimelineStatus(
//                        statusId = it.statusId,
//                        hashTag = hashTag,
//                        accountId = it.account.accountId,
//                        pollId = it.poll?.pollId,
//                        boostedStatusId = it.boostedStatus?.statusId,
//                        boostedStatusAccountId = it.boostedStatus?.account?.accountId,
//                        boostedPollId = it.boostedStatus?.poll?.pollId,
//                    )
//                })
//            }
//
//            // There seems to be some race condition for refreshes.  Subsequent pages do
//            // not get loaded because once we return a mediator result, the next append
//            // and prepend happen right away.  The paging source doesn't have enough time
//            // to collect the initial page from the database, so the [state] we get as
//            // a parameter in this load method doesn't have any data in the pages, so
//            // it's assumed we've reached the end of pagination, and nothing gets loaded
//            // ever again.
//            if (loadType == LoadType.REFRESH) {
//                delay(REFRESH_DELAY)
//            }
//
//            RemoteMediator.MediatorResult.Success(
//                endOfPaginationReached = when (loadType) {
//                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
//                    LoadType.REFRESH,
//                    LoadType.APPEND -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
//                }
//            )
//        } catch (e: Exception) {
//            RemoteMediator.MediatorResult.Error(e)
//        }
//    }
//}

private const val REFRESH_DELAY = 200L