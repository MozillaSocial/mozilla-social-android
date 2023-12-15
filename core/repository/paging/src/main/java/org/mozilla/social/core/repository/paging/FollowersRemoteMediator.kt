package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.common.getMaxIdValue
import org.mozilla.social.core.database.model.entities.accountCollections.Follower
import org.mozilla.social.core.database.model.entities.accountCollections.FollowerWrapper
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.FollowersRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FollowersRemoteMediator(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val followersRepository: FollowersRepository,
    private val relationshipRepository: RelationshipRepository,
    private val accountId: String,
) : RemoteMediator<Int, FollowerWrapper>() {
    private var nextKey: String? = null
    private var nextPositionIndex = 0

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FollowerWrapper>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        accountRepository.getAccountFollowers(
                            accountId = accountId,
                            olderThanId = null,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        if (nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        accountRepository.getAccountFollowers(
                            accountId = accountId,
                            olderThanId = nextKey,
                            loadSize = pageSize,
                        )
                    }
                }

            val relationships = response.accounts.getRelationships(accountRepository)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    followersRepository.deleteFollowers(accountId)
                    nextPositionIndex = 0
                }

                accountRepository.insertAll(response.accounts)
                relationshipRepository.insertAll(relationships)
                followersRepository.insertAll(
                    response.accounts.mapIndexed { index, account ->
                        Follower(
                            accountId = accountId,
                            followerAccountId = account.accountId,
                            position = nextPositionIndex + index,
                        )
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

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}

