package social.firefly.core.repository.paging.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import social.firefly.common.getMaxIdValue
import social.firefly.core.repository.mastodon.DomainBlocksRepository

class DomainBlocksPagingSource(
    private val domainBlocksRepository: DomainBlocksRepository,
) : PagingSource<String, String>() {

    override fun getRefreshKey(state: PagingState<String, String>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, String> {
        try {
            val response = domainBlocksRepository.getDomainBlocks(
                maxId = params.key,
                limit = params.loadSize,
            )
            return LoadResult.Page(
                data = response.items,
                prevKey = null, // Only paging forward.
                nextKey = response.pagingLinks?.getMaxIdValue()
            )
        } catch (e: Exception) {
            throw e
        }
    }
}