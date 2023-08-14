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
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.model.Status

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
) : RemoteMediator<String, DatabaseStatus>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, DatabaseStatus>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    //TODO refresh.  When is this called?
                    pageSize = state.config.initialLoadSize
                    timelineRepository.getHomeTimeline(
                        olderThanId = null,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize, //TODO is this right?
                    )
                }

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHomeTimeline(
                        olderThanId = null,
                        immediatelyNewerThanId = firstItem.statusId,
                        loadSize = pageSize,
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    timelineRepository.getHomeTimeline(
                        olderThanId = lastItem.statusId,
                        immediatelyNewerThanId = null,
                        loadSize = pageSize,
                    )
                }
            }.getInReplyToAccountNames()

            socialDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    socialDatabase.statusDao().deleteHomeTimeline()
                }

                socialDatabase.statusDao().insertAll(response.map {
                    it.toDatabaseModel(isInHomeFeed = true)
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
