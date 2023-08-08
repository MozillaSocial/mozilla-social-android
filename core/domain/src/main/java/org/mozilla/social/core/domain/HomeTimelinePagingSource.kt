package org.mozilla.social.core.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

/**
 * The key Pair<String, String> the first string is the older than ID, the second is the newer than ID
 */
class HomeTimelinePagingSource(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository
) : PagingSource<Pair<String?, String?>, Post>() {

    /**
     * Set the key to the status ID of 10 posts above the current anchor.
     * This should load 10 posts on either side of it
     */
    override fun getRefreshKey(state: PagingState<Pair<String?, String?>, Post>): Pair<String?, String?> {
        val indexOfNewKey = ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
        if (indexOfNewKey == 0) {
            return Pair(null, null)
        }
        val post = state.closestItemToPosition(indexOfNewKey)
        return Pair(post?.status?.id, null)
    }

    override suspend fun load(params: LoadParams<Pair<String?, String?>>): LoadResult<Pair<String?, String?>, Post> {
        return try {
            val nextOlderThanId = params.key?.first
            val previousNewerThanId = params.key?.second
            val response = timelineRepository.getHomeTimeline(
                nextOlderThanId,
                previousNewerThanId,
            ).getInReplyToAccountNames()
            LoadResult.Page(
                data = response,
                prevKey = Pair(null, response.first().status.id),
                nextKey = Pair(response.last().status.id, null)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
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