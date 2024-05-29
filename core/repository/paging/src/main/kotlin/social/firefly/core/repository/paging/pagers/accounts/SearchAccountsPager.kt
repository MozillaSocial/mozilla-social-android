package social.firefly.core.repository.paging.pagers.accounts

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.accountCollections.SearchedAccount
import social.firefly.core.database.model.entities.accountCollections.SearchedAccountWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.SearchType
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount
import social.firefly.core.repository.paging.IndexBasedPager

class SearchAccountsPager(
    private val searchRepository: SearchRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val query: String,
) : IndexBasedPager<DetailedAccountWrapper, SearchedAccountWrapper> {
    override fun mapDbObjectToExternalModel(dbo: SearchedAccountWrapper): DetailedAccountWrapper =
        dbo.toDetailedAccount()

    override suspend fun saveLocally(items: List<PageItem<DetailedAccountWrapper>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            val accounts = items.map { it.item.account }
            val relationships = items.map { it.item.relationship }

            accountRepository.insertAll(accounts)
            relationshipRepository.insertAll(relationships)
            searchRepository.insertAllAccounts(
                items.map {
                    SearchedAccount(
                        accountId = it.item.account.accountId,
                        position = it.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<DetailedAccountWrapper> {
        val response = searchRepository.search(
            query = query,
            type = SearchType.Accounts,
            limit = limit,
            offset = offset,
        )

        val relationships = accountRepository.getAccountRelationships(response.accounts.map { it.accountId })

        return response.accounts.mapNotNull { account ->
            relationships.find { account.accountId == it.accountId }?.let { relationship ->
                DetailedAccountWrapper(
                    account = account,
                    relationship = relationship
                )
            }
        }
    }

    override fun pagingSource(): PagingSource<Int, SearchedAccountWrapper> =
        searchRepository.getAccountsPagingSource()
}