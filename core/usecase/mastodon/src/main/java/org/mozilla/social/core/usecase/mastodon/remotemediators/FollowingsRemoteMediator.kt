package org.mozilla.social.core.usecase.mastodon.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.database.model.accountCollections.Followee
import org.mozilla.social.core.database.model.accountCollections.FolloweeWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.FollowingsRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId

@OptIn(ExperimentalPagingApi::class)
class FollowingsRemoteMediator internal constructor(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val followingsRepository: FollowingsRepository,
    private val relationshipRepository: RelationshipRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val accountId: String,
) : RemoteMediator<Int, FolloweeWrapper>() {

    private val loggedInUserAccountId = getLoggedInUserAccountId()

    private var nextKey: String? = null

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FolloweeWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    pageSize = state.config.initialLoadSize
                    accountRepository.getAccountFollowing(
                        accountId = accountId,
                        olderThanId = null,
                        loadSize = pageSize,
                    )
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    if (nextKey == null)
                        return MediatorResult.Success(endOfPaginationReached = true)
                    accountRepository.getAccountFollowing(
                        accountId = accountId,
                        olderThanId = nextKey,
                        loadSize = pageSize,
                    )
                }
            }

            val relationships = response.accounts.getRelationships(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    followingsRepository.deleteFollowings(accountId)
                }

                accountRepository.insertAll(response.accounts)
                relationshipRepository.insertAll(relationships)
                followingsRepository.insertAll(
                    response.accounts.map {
                        Followee(
                            accountId = accountId,
                            followingAccountId = it.accountId,
                            relationshipAccountId = loggedInUserAccountId,
                        )
                    }
                )

            }

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
                endOfPaginationReached = when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                    LoadType.REFRESH,
                    LoadType.APPEND -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                }
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}

private const val REFRESH_DELAY = 200L