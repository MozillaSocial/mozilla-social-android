package social.firefly.core.repository.paging.pagers.accounts

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.accountCollections.Followee
import social.firefly.core.database.model.entities.accountCollections.FolloweeWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FollowingsRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount
import social.firefly.core.repository.paging.IdBasedPager

class FollowingsPager(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val followingsRepository: FollowingsRepository,
    private val relationshipRepository: RelationshipRepository,
    private val accountId: String,
) : IdBasedPager<DetailedAccountWrapper, FolloweeWrapper> {

    override fun mapDbObjectToExternalModel(dbo: FolloweeWrapper): DetailedAccountWrapper =
        dbo.toDetailedAccount()

    override suspend fun saveLocally(items: List<PageItem<DetailedAccountWrapper>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                followingsRepository.deleteFollowings(accountId)
            }

            val accounts = items.map { it.item.account }
            val relationships = items.map { it.item.relationship }

            accountRepository.insertAll(accounts)
            relationshipRepository.insertAll(relationships)
            followingsRepository.insertAll(
                items.map { item ->
                    Followee(
                        accountId = accountId,
                        followeeAccountId = item.item.account.accountId,
                        position = item.position,
                    )
                },
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<DetailedAccountWrapper> {
        val response = accountRepository.getAccountFollowing(
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

    override fun pagingSource(): PagingSource<Int, FolloweeWrapper> =
        followingsRepository.getPagingSource(accountId)
}