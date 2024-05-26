package social.firefly.core.repository.paging.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.Rel
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class LocalTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
    private val databaseDelegate: DatabaseDelegate,
) : RemoteMediator<Int, LocalTimelineStatusWrapper>() {

    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalTimelineStatusWrapper>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
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
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        timelineRepository.getPublicTimeline(
                            localOnly = true,
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.localTimelineStatus.statusId,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        timelineRepository.getPublicTimeline(
                            localOnly = true,
                            olderThanId = lastItem.localTimelineStatus.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.items)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    timelineRepository.deleteLocalTimeline()
                }

                saveStatusToDatabase(result)
                timelineRepository.insertAllIntoLocalTimeline(result)
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
                endOfPaginationReached =
                when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                    LoadType.REFRESH,
                    LoadType.APPEND,
                    -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                },
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }
}
