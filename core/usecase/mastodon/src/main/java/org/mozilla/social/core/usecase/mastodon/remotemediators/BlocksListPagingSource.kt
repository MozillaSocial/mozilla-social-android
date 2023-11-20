package org.mozilla.social.core.usecase.mastodon.remotemediators

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.mozilla.social.common.HeaderLink
import org.mozilla.social.common.getNext
import org.mozilla.social.common.toHeaderLink
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.BlocksRepository

class BlocksListPagingSource(
    private val blocksRepository: BlocksRepository,
) : PagingSource<HeaderLink, Account>() {
    override fun getRefreshKey(state: PagingState<HeaderLink, Account>): HeaderLink {
        // TODO@DA not sure how to set this up
//        val anchor = state.anchorPosition?.let { state.closestPageToPosition(it) }
        return HeaderLink(maxId = null, sinceId = null, minId = null, limit = null)
    }

    override suspend fun load(params: LoadParams<HeaderLink>): LoadResult<HeaderLink, Account> {
        return try {

            val headerLink: HeaderLink = params.key ?: HeaderLink(null, null, null, null)
            val response = blocksRepository.getBlocks(
                maxId = headerLink.maxId,
                sinceId = headerLink.sinceId,
                minId = headerLink.minId,
                limit = headerLink.limit
            )
            LoadResult.Page(
                data = response.accounts,
                prevKey = null,
                nextKey = response.pagingLinks?.getNext()?.toHeaderLink(),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
