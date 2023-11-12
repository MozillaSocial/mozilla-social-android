package org.mozilla.social.feature.account

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import org.mozilla.social.common.Rel
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.storage.mastodon.DatabaseDelegate
import org.mozilla.social.core.storage.mastodon.timeline.LocalAccountTimelineRepository
import org.mozilla.social.core.usecase.mastodon.remotemediators.getInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusesToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class AccountTimelineRemoteMediator(
    private val accountRepository: AccountRepository,
    private val saveStatusesToDatabase: SaveStatusesToDatabase,
    private val localAccountTimelineRepository: LocalAccountTimelineRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val accountId: String,
    private val timelineType: StateFlow<TimelineType>,
) : RemoteMediator<Int, AccountTimelineStatusWrapper>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AccountTimelineStatusWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    pageSize = state.config.initialLoadSize
                    accountRepository.getAccountStatuses(
                        accountId = accountId,
                        olderThanId = null,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                        onlyMedia = timelineType.value == TimelineType.MEDIA,
                        excludeReplies = timelineType.value == TimelineType.POSTS,
                    )
                }

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    accountRepository.getAccountStatuses(
                        accountId = accountId,
                        olderThanId = null,
                        immediatelyNewerThanId = firstItem.status.statusId,
                        loadSize = pageSize,
                        onlyMedia = timelineType.value == TimelineType.MEDIA,
                        excludeReplies = timelineType.value == TimelineType.POSTS,
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    accountRepository.getAccountStatuses(
                        accountId = accountId,
                        olderThanId = lastItem.status.statusId,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                        onlyMedia = timelineType.value == TimelineType.MEDIA,
                        excludeReplies = timelineType.value == TimelineType.POSTS,
                    )
                }
            }

            val result = response.statuses.getInReplyToAccountNames(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localAccountTimelineRepository.deleteAccountTimeline(accountId)
                }

                saveStatusesToDatabase(result)

                localAccountTimelineRepository.insertAll(result)
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
