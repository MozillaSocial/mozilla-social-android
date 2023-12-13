package org.mozilla.social.core.usecase.mastodon.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.common.getMaxIdValue
import org.mozilla.social.core.database.model.entities.accountCollections.BlockWrapper
import org.mozilla.social.core.model.BlockedUser
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseBlock
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class BlocksListRemoteMediator(
    private val accntRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
    private val blocksRepository: BlocksRepository,
) : RemoteMediator<Int, BlockWrapper>() {
    private var nextKey: String? = null
    private var nextPositionIndex = 0

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BlockWrapper>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        blocksRepository.getBlocks(
                            maxId = null,
                            limit = pageSize,
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        if (nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        blocksRepository.getBlocks(
                            maxId = nextKey,
                            limit = pageSize,
                        )
                    }
                }

            val relationships = accntRepository.getAccountRelationships(response.accounts.map { it.accountId })

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
//                    blocksRepository.deleteFollowers(accountId)
                    nextPositionIndex = 0
                }

                // TODO@DA are these added automatically?
//                accntRepository.insertAll(response.accounts)
                relationshipRepository.insertAll(relationships)
                blocksRepository.insertAll(
                    response.accounts.mapIndexed { index, account ->
                        account.toDatabaseBlock(isBlocked = true, position = index)
                    },
                )
            }

            nextKey = response.pagingLinks?.getMaxIdValue()
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

            @Suppress("KotlinConstantConditions")
            MediatorResult.Success(
                endOfPaginationReached =
                when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                    LoadType.REFRESH,
                    LoadType.APPEND,
                    -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                },
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }
}
//
//class BlocksAccountListRefresher(
//    private val accountRepository: AccountRepository,
//    private val databaseDelegate: DatabaseDelegate,
//    private val relationshipRepository: RelationshipRepository,
//    private val blocksRepository: BlocksRepository,
//    private val accountId: String,
////    private
//): AccountsListRefresher {
//    override suspend fun getPagingWrapper(
//        accountId: String,
//        olderThanId: String?,
//        newerThanId: String?,
//        loadSize: Int?
//    ): AccountPagingWrapper {
//        return blocksRepository.getBlocks()
//    }
//
//    override suspend fun saveAll(accounts: List<Account>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun clearSaved(accountId: String): AccountPagingWrapper {
//        TODO("Not yet implemented")
//    }
//}
//
//
//interface AccountsListRefresher {
//
//    suspend fun getPagingWrapper(
//        accountId: String,
//        olderThanId: String?,
//        newerThanId: String?,
//        loadSize: Int?
//    ): AccountPagingWrapper
//
//    suspend fun saveAll(accounts: List<Account>)
//
//    suspend fun clearSaved(accountId: String): AccountPagingWrapper
//
//}

private const val REFRESH_DELAY = 200L
