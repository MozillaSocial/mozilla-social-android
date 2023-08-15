package org.mozilla.social.core.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatusWrapper
import org.mozilla.social.model.Status

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
) : RemoteMediator<Int, HomeTimelineStatusWrapper>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HomeTimelineStatusWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    pageSize = state.config.initialLoadSize
                    timelineRepository.getHomeTimeline(
                        olderThanId = null,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHomeTimeline(
                        olderThanId = null,
                        immediatelyNewerThanId = firstItem.status.statusId,
                        loadSize = pageSize,
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHomeTimeline(
                        olderThanId = lastItem.status.statusId,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }
            }.getInReplyToAccountNames()

            socialDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    socialDatabase.homeTimelineDao().deleteHomeTimeline()
                }

                val boostedStatuses = response.mapNotNull { it.boostedStatus }
                socialDatabase.accountsDao().insertAll(boostedStatuses.map {
                    it.account.toDatabaseModel()
                })
                socialDatabase.statusDao().insertAll(boostedStatuses.map {
                    it.toDatabaseModel()
                })
                socialDatabase.accountsDao().insertAll(response.map {
                    it.account.toDatabaseModel()
                })
                socialDatabase.statusDao().insertAll(response.map {
                    it.toDatabaseModel()
                })
                socialDatabase.homeTimelineDao().insertAll(response.map {
                    HomeTimelineStatus(
                        statusId = it.statusId,
                        createdAt = it.createdAt,
                        accountId = it.account.accountId,
                        boostedStatusId = it.boostedStatus?.statusId,
                        boostedStatusAccountId = it.boostedStatus?.account?.accountId,
                    )
                })
            }

            MediatorResult.Success(
                endOfPaginationReached = response.size != pageSize
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun List<Status>.getInReplyToAccountNames(): List<Status> =
        coroutineScope {
            map { status ->
                // get in reply to account names
                async {
                    status.copy(
                        inReplyToAccountName = status.inReplyToAccountId?.let { accountId ->
                            accountRepository.getAccount(accountId).displayName
                        }
                    )
                }
            }.map {
                it.await()
            }
        }
}
