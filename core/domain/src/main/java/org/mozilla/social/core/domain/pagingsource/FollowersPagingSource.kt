package org.mozilla.social.core.domain.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.mozilla.social.common.Rel
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.model.Account

class FollowersPagingSource(
    private val accountRepository: AccountRepository,
    private val accountId: String,
) : PagingSource<String, Account>() {

    override val keyReuseSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<String, Account>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Account> {
        return try {
            val response = accountRepository.getAccountFollowers(
                accountId = accountId,
                olderThanId = params.key,
                loadSize = params.loadSize,
            )
            // We get the next item from the links header in the response.
            val links = response.link?.parseMastodonLinkHeader()
            LoadResult.Page(
                data = response.accounts,
                prevKey = null,
                nextKey = links?.find { it.rel == Rel.NEXT }
                    ?.link
                    ?.substringAfter("max_id=")
                    ?.substringBefore("&")
                    ?.substringBefore(">")
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}