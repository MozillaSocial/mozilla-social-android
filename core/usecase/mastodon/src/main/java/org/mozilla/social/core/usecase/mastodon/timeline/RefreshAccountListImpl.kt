package org.mozilla.social.core.usecase.mastodon.timeline

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.common.getMaxIdValue
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.paging.AccountPagingWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import timber.log.Timber

// TODO@DA delete file
///**
// * Refresh a list of accounts, like for example the blocks list
// *
// * @property accountRepository
// * @property databaseDelegate
// * @property relationshipRepository
// * @property accountId
// * @property getAccountListPagingWrapper
// * @property clearSaved
// * @property saveAll
// * @constructor Create empty Refresh account list
// */
//open class RefreshAccountListImpl internal constructor(
//    private val accountRepository: AccountRepository,
//    private val databaseDelegate: DatabaseDelegate,
//    private val relationshipRepository: RelationshipRepository,
//    private val accountsListRefresher: AccountsListRefresher,
//    private val accountId: String,
//) {
//    private var nextKey: String? = null
//    private var nextPositionIndex = 0
//
//    @OptIn(ExperimentalPagingApi::class)
//    suspend fun invoke(
//        loadType: LoadType,
//        state: PagingState<String, Account>,
//    ): RemoteMediator.MediatorResult {
//        return try {
//            var pageSize: Int = state.config.pageSize
//            val response: AccountPagingWrapper =
//                when (loadType) {
//                    LoadType.REFRESH -> {
//                        pageSize = state.config.initialLoadSize
//                        accountsListRefresher.getPagingWrapper(
//                            accountId, null, null, pageSize,
//                        )
//                    }
//
//                    LoadType.PREPEND -> {
//                        return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
//                    }
//
//                    LoadType.APPEND -> {
//                        if (nextKey == null) {
//                            return RemoteMediator.MediatorResult.Success(endOfPaginationReached = true)
//                        }
//                        accountsListRefresher.getPagingWrapper(
//                            accountId, nextKey, null, pageSize,
//                        )
//                    }
//                }
//
//            val relationships =
//                accountRepository.getAccountRelationships(response.accounts.map { it.accountId })
//
//            databaseDelegate.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    accountsListRefresher.clearSaved(accountId)
//                    nextPositionIndex = 0
//                }
//
//                accountsListRefresher.saveAll(response.accounts)
//            }
//
//            nextKey = response.pagingLinks?.getMaxIdValue()
//            nextPositionIndex += response.accounts.size
//
//            // There seems to be some race condition for refreshes.  Subsequent pages do
//            // not get loaded because once we return a mediator result, the next append
//            // and prepend happen right away.  The paging source doesn't have enough time
//            // to collect the initial page from the database, so the [state] we get as
//            // a parameter in this load method doesn't have any data in the pages, so
//            // it's assumed we've reached the end of pagination, and nothing gets loaded
//            // ever again.
//            if (loadType == LoadType.REFRESH) {
//                delay(REFRESH_DELAY)
//            }
//
//            @Suppress("KotlinConstantConditions")
//            RemoteMediator.MediatorResult.Success(
//                endOfPaginationReached =
//                when (loadType) {
//                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
//                    LoadType.REFRESH,
//                    LoadType.APPEND,
//                    -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
//                },
//            )
//        } catch (e: Exception) {
//            Timber.e(e)
//            RemoteMediator.MediatorResult.Error(e)
//        }
//    }
//
//    companion object {
//        private const val REFRESH_DELAY = 200L
//    }
//}

//interface GetListOfAccounts {
//    suspend fun invoke(
//        accountId: String,
//        olderThanId: String?,
//        newerThanId: String?,
//        loadSize: Int?
//    ): List<Account>
//}


//typealias ClearSaved = suspend (
//    accountId: String,
//) -> AccountPagingWrapper
//
//typealias SaveAll = suspend (accounts: List<Account>) -> Unit

//@OptIn(ExperimentalPagingApi::class)
//interface RemoteMediatorWrapper {
//    suspend fun invoke(
//        loadType: LoadType,
//        state: PagingState<Int, Account>,
//    ): RemoteMediator.MediatorResult
//}