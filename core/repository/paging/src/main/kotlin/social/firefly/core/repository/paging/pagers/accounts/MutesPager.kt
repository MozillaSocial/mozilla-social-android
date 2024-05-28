package social.firefly.core.repository.paging.pagers.accounts

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.wrappers.AccountAndRelationship
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.MutesRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.model.block.toExternal
import social.firefly.core.repository.mastodon.model.status.toDatabaseMute
import social.firefly.core.repository.paging.IdBasedPager

class MutesPager(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
    private val mutesRepository: MutesRepository,
) : IdBasedPager<AccountAndRelationship, MuteWrapper> {
    override fun mapDbObjectToExternalModel(dbo: MuteWrapper): AccountAndRelationship =
        dbo.toExternal()

    override suspend fun saveLocally(items: List<PageItem<AccountAndRelationship>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                mutesRepository.deleteAll()
            }

            accountRepository.insertAll(items.map { it.item.account })
            relationshipRepository.insertAll(items.map { it.item.relationship })
            mutesRepository.insertAll(
                items.map {
                    it.item.account.toDatabaseMute(position = it.position)
                },
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<AccountAndRelationship> {
        val response = mutesRepository.getMutes(
            maxId = nextKey,
            limit = limit,
        )

        val relationships = accountRepository.getAccountRelationships(response.items.map { it.accountId })

        val blockedUsers = response.items.mapNotNull { account ->
            relationships.find { account.accountId == it.accountId }?.let { relationship ->
                AccountAndRelationship(
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

    override fun pagingSource(): PagingSource<Int, MuteWrapper> = mutesRepository.getPagingSource()
}