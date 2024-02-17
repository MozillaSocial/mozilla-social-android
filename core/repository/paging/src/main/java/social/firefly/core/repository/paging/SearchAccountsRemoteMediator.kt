package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.core.database.model.entities.accountCollections.SearchedAccountWrapper
import social.firefly.core.model.SearchType
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.model.search.toSearchedAccount
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SearchAccountsRemoteMediator(
    private val searchRepository: SearchRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val query: String,
) : RemoteMediator<Int, SearchedAccountWrapper>() {
    private var nextPositionIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchedAccountWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        nextPositionIndex = 0
                        pageSize = state.config.initialLoadSize
                        searchRepository.search(
                            query = query,
                            type = SearchType.Accounts,
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        searchRepository.search(
                            query = query,
                            type = SearchType.Accounts,
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }
                }

            val relationships = response.accounts.getRelationships(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    nextPositionIndex = 0
                }

                accountRepository.insertAll(response.accounts)
                relationshipRepository.insertAll(relationships)
                searchRepository.insertAllAccounts(
                    response.accounts.toSearchedAccount(startIndex = nextPositionIndex)
                )
            }

            nextPositionIndex += response.accounts.size

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(REFRESH_DELAY)
            }

            MediatorResult.Success(
                endOfPaginationReached = when (loadType) {
                    LoadType.REFRESH,
                    LoadType.APPEND -> response.accounts.isEmpty()
                    else -> true
                },
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}