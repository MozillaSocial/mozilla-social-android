package org.mozilla.social.core.usecase.mastodon.account

import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.model.paging.FollowersPagingWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository

class GetDetailedFollowing(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun <T> invoke(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
        transform: (
            followersPagingWrapper: FollowersPagingWrapper,
            relationships: List<Relationship>
        ) -> List<T>,
    ): List<T> {
        val followers = accountRepository.getAccountFollowing(
            accountId = accountId,
            olderThanId = olderThanId,
            newerThanId = newerThanId,
            loadSize = loadSize,
        )
        val relationships = accountRepository.getAccountRelationships(
            followers.accounts.map { it.accountId }
        )
        return transform(followers, relationships)
    }
}