package org.mozilla.social.core.usecase.mastodon.timeline

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.database.model.entities.statusCollections.FederatedTimelineStatusWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.remotemediators.getInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

class RefreshFederatedTimeline internal constructor(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    @OptIn(ExperimentalPagingApi::class)
    suspend operator fun invoke(
        loadType: LoadType,
        state: PagingState<Int, FederatedTimelineStatusWrapper>,
    ): RemoteMediator.MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        timelineRepository.getPublicTimeline(
                            federatedOnly = true,
                            olderThanId = null,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.PREPEND -> {
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
                        timelineRepository.getPublicTimeline(
                            federatedOnly = true,
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.status.statusId,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
                        timelineRepository.getPublicTimeline(
                            federatedOnly = true,
                            olderThanId = lastItem.status.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                        )
                    }
                }

            val result = response.statuses.getInReplyToAccountNames(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    timelineRepository.deleteFederatedTimeline()
                }

                saveStatusToDatabase(result)
                timelineRepository.insertAllIntoFederatedTimeline(result)
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

            RemoteMediator.MediatorResult.Success(
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
            RemoteMediator.MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}
