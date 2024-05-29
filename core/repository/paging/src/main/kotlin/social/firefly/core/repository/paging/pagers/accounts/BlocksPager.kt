package social.firefly.core.repository.paging.pagers.accounts

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.accountCollections.BlockWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.BlocksRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.model.block.toExternal
import social.firefly.core.repository.mastodon.model.status.toDatabaseBlock
import social.firefly.core.repository.paging.IdBasedPager

class BlocksPager(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
    private val blocksRepository: BlocksRepository,
) : IdBasedPager<DetailedAccountWrapper, BlockWrapper> {
    override fun mapDbObjectToExternalModel(dbo: BlockWrapper): DetailedAccountWrapper =
        dbo.toExternal()

    override suspend fun saveLocally(items: List<PageItem<DetailedAccountWrapper>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                blocksRepository.deleteAll()
            }

            accountRepository.insertAll(items.map { it.item.account })
            relationshipRepository.insertAll(items.map { it.item.relationship })
            blocksRepository.insertAll(
                items.map {
                    it.item.account.toDatabaseBlock(position = it.position)
                },
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<DetailedAccountWrapper> {
        val response = blocksRepository.getBlocks(
            maxId = nextKey,
            limit = limit,
        )

        val relationships = accountRepository.getAccountRelationships(response.items.map { it.accountId })

        val blockedUsers = response.items.mapNotNull { account ->
            relationships.find { account.accountId == it.accountId }?.let { relationship ->
                DetailedAccountWrapper(
                    account = account,
                    relationship = relationship
                )
            }
        }

        return MastodonPagedResponse(
            items = blockedUsers,
            pagingLinks = response.pagingLinks
        )
    }

    override fun pagingSource(): PagingSource<Int, BlockWrapper> = blocksRepository.getPagingSource()
}