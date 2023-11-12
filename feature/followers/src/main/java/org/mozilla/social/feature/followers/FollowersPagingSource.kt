package org.mozilla.social.feature.followers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.mozilla.social.common.Rel
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.model.Account
import timber.log.Timber

class FollowersPagingSource(
    private val accountRepository: AccountRepository,
    private val accountId: String,
    private val followerScreenType: FollowerScreenType,
) : PagingSource<String, Account>() {

    override fun getRefreshKey(state: PagingState<String, Account>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Account> {
        return try {
            val response = when(followerScreenType) {
                FollowerScreenType.FOLLOWERS -> accountRepository.getAccountFollowers(
                    accountId = accountId,
                    olderThanId = params.key,
                    loadSize = params.loadSize,
                )
                FollowerScreenType.FOLLOWING -> accountRepository.getAccountFollowing(
                    accountId = accountId,
                    olderThanId = params.key,
                    loadSize = params.loadSize,
                )
            }
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
            Timber.e(e)
            LoadResult.Error(e)
        }
    }
}