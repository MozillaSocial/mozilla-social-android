package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.SearchDao
import org.mozilla.social.core.database.model.entities.accountCollections.FollowerWrapper
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccountWrapper
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatusWrapper
import org.mozilla.social.core.database.model.entities.statusCollections.toStatusWrapper
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.SearchResult
import org.mozilla.social.core.model.SearchResultDetailed
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.network.mastodon.SearchApi
import org.mozilla.social.core.repository.mastodon.model.account.toDetailedAccount
import org.mozilla.social.core.repository.mastodon.model.hashtag.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.search.toExternal
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

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
    ) : SearchResult = searchApi.search(
        query = query,
        type = type?.value,
        resolve = resolve,
        accountId = accountId,
        excludeUnreviewed = excludeUnreviewed,
        limit = limit,
        offset = offset,
    ).toExternal()

    fun insertAllAccounts(
        searchedAccounts: List<SearchedAccount>
    ) = searchDao.upsertAccounts(searchedAccounts)

    fun insertAllStatuses(
        searchedStatuses: List<SearchedStatus>
    ) = searchDao.upsertStatuses(searchedStatuses)

    fun insertAllHashTags(
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
                it.toStatusWrapper().toExternalModel()
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

    @ExperimentalPagingApi
    fun getAccountsPager(
        remoteMediator: RemoteMediator<Int, SearchedAccountWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<DetailedAccountWrapper>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            searchDao.accountsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toDetailedAccount()
            }
        }

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
                it.toStatusWrapper().toExternalModel()
            }
        }

    @ExperimentalPagingApi
    fun getHashTagsPager(
        remoteMediator: RemoteMediator<Int, SearchedHashTagWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<HashTag>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            searchDao.hashTagsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.hashTag.toExternalModel()
            }
        }
}
