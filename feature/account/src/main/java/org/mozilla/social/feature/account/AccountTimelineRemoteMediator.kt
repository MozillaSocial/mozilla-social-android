package org.mozilla.social.feature.account

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.domain.remotemediators.getInReplyToAccountNames

@OptIn(ExperimentalPagingApi::class)
class AccountTimelineRemoteMediator(
    private val accountRepository: AccountRepository,
    private val statusRepository: StatusRepository,
    private val socialDatabase: SocialDatabase,
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
            }.getInReplyToAccountNames(accountRepository)

            socialDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    socialDatabase.accountTimelineDao().deleteAccountTimeline(accountId)
                }

                statusRepository.saveStatusesToDatabase(response)

                socialDatabase.accountTimelineDao().insertAll(response.map {
                    AccountTimelineStatus(
                        statusId = it.statusId,
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

enum class TimelineType {
    POSTS,
    POSTS_AND_REPLIES,
    MEDIA,
}