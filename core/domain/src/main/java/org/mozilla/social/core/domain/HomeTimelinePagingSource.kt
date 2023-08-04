package org.mozilla.social.core.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

class HomeTimelinePagingSource(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository
) : PagingSource<String, Post>() {

    //TODO I don't fully understand this...
    // trying to get the previous page's last item's status ID
    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val currentPage = state.closestPageToPosition(anchorPosition)
            val currentPageIndex = state.pages.indexOf(currentPage)
            if (currentPageIndex > 0) {
                val previousPage = state.pages[currentPageIndex - 1]
                previousPage.data.last().status.id
            } else {
                null
            }
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            val nextOlderThanId = params.key
            val response = timelineRepository.getHomeTimeline(
                nextOlderThanId,
            ).getInReplyToAccountNames()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.last().status.id
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