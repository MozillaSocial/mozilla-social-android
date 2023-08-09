package org.mozilla.social.core.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val homeTimelineListHolder: HomeTimelineListHolder,
    private val invalidate: () -> Unit,
) : RemoteMediator<String, Post>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, Post>
    ): MediatorResult {
        return try {
            println("johnny remote mediator")
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    println("johnny remote mediator refresh")
                    null
                }
                LoadType.PREPEND -> {
                    println("johnny remote prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.status.id
                }
            }
            val response = timelineRepository.getHomeTimeline(
                olderThanId = loadKey
            ).getInReplyToAccountNames()

            homeTimelineListHolder.timeline.addAll(response)

            println("johnny remote mediator response size ${response.size}")

            invalidate()

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: Exception) {
            println("johnny remote error")
            MediatorResult.Error(e)
        }
    }

    private suspend fun List<Status>.getInReplyToAccountNames(): List<Post> =
        coroutineScope {
            map { status ->
                // get in reply to account names
                async {
                    Post(
                        status = status,
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