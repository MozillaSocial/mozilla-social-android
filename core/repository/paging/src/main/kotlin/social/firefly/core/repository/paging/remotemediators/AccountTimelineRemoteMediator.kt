package social.firefly.core.repository.paging.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.Rel
import social.firefly.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class AccountTimelineRemoteMediator(
    private val accountId: String,
    private val timelineType: AccountTimelineType,
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : RemoteMediator<Int, AccountTimelineStatusWrapper>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AccountTimelineStatusWrapper>,
    ): MediatorResult {
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
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        accountRepository.getAccountStatuses(
                            accountId = accountId,
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.accountTimelineStatus.statusId,
                            loadSize = pageSize,
                            onlyMedia = timelineType == AccountTimelineType.MEDIA,
                            excludeReplies = timelineType == AccountTimelineType.POSTS,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        accountRepository.getAccountStatuses(
                            accountId = accountId,
                            olderThanId = lastItem.accountTimelineStatus.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                            onlyMedia = timelineType == AccountTimelineType.MEDIA,
                            excludeReplies = timelineType == AccountTimelineType.POSTS,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.items)

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

            MediatorResult.Success(
                endOfPaginationReached = when (loadType) {
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

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}
