package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.database.model.entities.statusCollections.HashTagTimelineStatusWrapper
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.status.GetInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase

@OptIn(ExperimentalPagingApi::class)
class HashTagTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
    private val hashTag: String,
) : RemoteMediator<Int, HashTagTimelineStatusWrapper>() {
    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HashTagTimelineStatusWrapper>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
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
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return MediatorResult.Success(endOfPaginationReached = true)
                        timelineRepository.getHashtagTimeline(
                            hashTag = hashTag,
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.hashTagTimelineStatus.statusId,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(endOfPaginationReached = true)
                        timelineRepository.getHashtagTimeline(
                            hashTag = hashTag,
                            olderThanId = lastItem.hashTagTimelineStatus.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.statuses)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    timelineRepository.deleteHashTagTimeline(hashTag)
                }

                saveStatusToDatabase(result)

                timelineRepository.insertAllIntoHashTagTimeline(hashTag, result)
            }

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(REFRESH_DELAY)
            }

            MediatorResult.Success(
                endOfPaginationReached =
                    when (loadType) {
                        LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                        LoadType.REFRESH,
                        LoadType.APPEND,
                        -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                    },
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}
