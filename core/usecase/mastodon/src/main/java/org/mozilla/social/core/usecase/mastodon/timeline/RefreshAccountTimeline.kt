package org.mozilla.social.core.usecase.mastodon.timeline

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.model.AccountTimelineType
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.remotemediators.getInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

class RefreshAccountTimeline internal constructor(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val accountId: String,
    private val timelineType: AccountTimelineType,
) {
    @OptIn(ExperimentalPagingApi::class)
    suspend operator fun invoke(
        loadType: LoadType,
        state: PagingState<Int, AccountTimelineStatusWrapper>,
    ): RemoteMediator.MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        accountRepository.getAccountStatuses(
                            accountId = accountId,
                            olderThanId = null,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                            onlyMedia = timelineType == AccountTimelineType.MEDIA,
                            excludeReplies = timelineType == AccountTimelineType.POSTS,
                        )
                    }

                    LoadType.PREPEND -> {
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
                        accountRepository.getAccountStatuses(
                            accountId = accountId,
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.status.statusId,
                            loadSize = pageSize,
                            onlyMedia = timelineType == AccountTimelineType.MEDIA,
                            excludeReplies = timelineType == AccountTimelineType.POSTS,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
                        accountRepository.getAccountStatuses(
                            accountId = accountId,
                            olderThanId = lastItem.status.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                            onlyMedia = timelineType == AccountTimelineType.MEDIA,
                            excludeReplies = timelineType == AccountTimelineType.POSTS,
                        )
                    }
                }

            val result = response.statuses.getInReplyToAccountNames(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    timelineRepository.deleteAccountTimeline(accountId, timelineType)
                }

                saveStatusToDatabase(result)
                timelineRepository.insertAllIntoAccountTimeline(result, timelineType)
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
