package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.SearchDao
import social.firefly.core.database.model.entities.accountCollections.SearchedAccount
import social.firefly.core.database.model.entities.accountCollections.SearchedAccountWrapper
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import social.firefly.core.database.model.entities.statusCollections.SearchedStatus
import social.firefly.core.database.model.entities.statusCollections.SearchedStatusWrapper
import social.firefly.core.model.Account
import social.firefly.core.model.HashTag
import social.firefly.core.model.SearchResult
import social.firefly.core.model.SearchResultDetailed
import social.firefly.core.model.SearchType
import social.firefly.core.model.Status
import social.firefly.core.network.mastodon.SearchApi
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.mastodon.model.search.toExternal
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class SearchRepository internal constructor(
    private val searchApi: SearchApi,
    private val searchDao: SearchDao,
    private val databaseDelegate: DatabaseDelegate,
) {
    suspend fun searchForAccounts(query: String): List<Account> {
        return searchApi.search(
            query,
            SearchType.Accounts.value,
        ).accounts.map { it.toExternalModel() }
    }

    suspend fun searchForHashtags(query: String): List<HashTag> {
        return searchApi.search(
            query,
            SearchType.Hashtags.value,
        ).hashtags.map { it.toExternalModel() }
    }

    suspend fun search(
        query: String,
        type: SearchType? = null,
        resolve: Boolean = false,
        accountId: String? = null,
        excludeUnreviewed: Boolean = false,
        limit: Int? = null,
        offset: Int? = null,
    ): SearchResult = searchApi.search(
        query = query,
        type = type?.value,
        resolve = resolve,
        accountId = accountId,
        excludeUnreviewed = excludeUnreviewed,
        limit = limit,
        offset = offset,
    ).toExternal()

    suspend fun insertAllAccounts(
        searchedAccounts: List<SearchedAccount>
    ) = searchDao.upsertAccounts(searchedAccounts)

    suspend fun insertAllStatuses(
        searchedStatuses: List<SearchedStatus>
    ) = searchDao.upsertStatuses(searchedStatuses)

    suspend fun insertAllHashTags(
        searchedHashTags: List<SearchedHashTag>
    ) = searchDao.upsertHashTags(searchedHashTags)

    fun getTopSearchResultsFlow(
        count: Int,
    ): Flow<SearchResultDetailed> = combine(
        searchDao.getTopAccountsFlow(count),
        searchDao.getTopStatusesFlow(count),
        searchDao.getTopHashTagsFlow(count),
    ) { accounts, statuses, hashtags ->
        SearchResultDetailed(
            accounts = accounts.map { it.toDetailedAccount() },
            statuses = statuses.map {
                it.status.toExternalModel()
            },
            hashtags = hashtags.map {
                it.hashTag.toExternalModel()
            },
        )
    }

    suspend fun deleteSearchResults() {
        databaseDelegate.withTransaction {
            searchDao.deleteAllSearchedAccounts()
            searchDao.deleteAllSearchedHashTags()
            searchDao.deleteAllSearchedStatues()
        }
    }

    fun getAccountsPagingSource(): PagingSource<Int, SearchedAccountWrapper> =
        searchDao.accountsPagingSource()

    @ExperimentalPagingApi
    fun getStatusesPager(
        remoteMediator: RemoteMediator<Int, SearchedStatusWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<Status>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            searchDao.statusesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }

    fun getHashTagPagingSource(): PagingSource<Int, SearchedHashTagWrapper> =
        searchDao.hashTagsPagingSource()
}
