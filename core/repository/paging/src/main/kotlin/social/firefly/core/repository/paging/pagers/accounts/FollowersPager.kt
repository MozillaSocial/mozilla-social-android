package social.firefly.core.repository.paging.pagers.accounts

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.accountCollections.Follower
import social.firefly.core.database.model.entities.accountCollections.FollowerWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FollowersRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount
import social.firefly.core.repository.paging.IdBasedPager

class FollowersPager(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val followersRepository: FollowersRepository,
    private val relationshipRepository: RelationshipRepository,
    private val accountId: String,
) : IdBasedPager<DetailedAccountWrapper, FollowerWrapper> {

    override fun mapDbObjectToExternalModel(dbo: FollowerWrapper): DetailedAccountWrapper =
        dbo.toDetailedAccount()

    override suspend fun saveLocally(items: List<PageItem<DetailedAccountWrapper>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                followersRepository.deleteFollowers(accountId)
            }

            val accounts = items.map { it.item.account }
            val relationships = items.map { it.item.relationship }

            accountRepository.insertAll(accounts)
            relationshipRepository.insertAll(relationships)
            followersRepository.insertAll(
                items.map { item ->
                    Follower(
                        accountId = accountId,
                        followerAccountId = item.item.account.accountId,
                        position = item.position,
                    )
                },
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<DetailedAccountWrapper> {
        val response = accountRepository.getAccountFollowers(
            accountId = accountId,
            maxId = nextKey,
            limit = limit,
        )

        val relationships = accountRepository.getAccountRelationships(response.items.map { it.accountId })

        val accounts = response.items.mapNotNull { account ->
            relationships.find { account.accountId == it.accountId }?.let { relationship ->
                DetailedAccountWrapper(
                    account = account,
                    relationship = relationship
                )
            }
        }

        return MastodonPagedResponse(
            items = accounts,
            pagingLinks = response.pagingLinks
        )
    }

    override fun pagingSource(): PagingSource<Int, FollowerWrapper> =
        followersRepository.getPagingSource(accountId)
}