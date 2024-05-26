package social.firefly.core.repository.paging.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.getMaxIdValue
import social.firefly.common.getNext
import social.firefly.common.getPrev
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.MutesRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.model.status.toDatabaseMute
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class MutesListRemoteMediator(
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
    private val mutesRepository: MutesRepository,
) : RemoteMediator<Int, MuteWrapper>() {
    private var nextKey: String? = null
    private var nextPositionIndex = 0

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MuteWrapper>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        mutesRepository.getMutes(
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
                        mutesRepository.getMutes(
                            maxId = nextKey,
                            limit = pageSize,
                        )
                    }
                }

            val relationships =
                accountRepository.getAccountRelationships(response.items.map { it.accountId })

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mutesRepository.deleteAll()
                    nextPositionIndex = 0
                }

                accountRepository.insertAll(response.items)
                relationshipRepository.insertAll(relationships)
                mutesRepository.insertAll(
                    response.items.mapIndexed { index, account ->
                        account.toDatabaseMute(position = nextPositionIndex + index)
                    },
                )
            }

            nextKey = response.pagingLinks?.getMaxIdValue()
            nextPositionIndex += response.items.size

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
            (MediatorResult.Success(
                endOfPaginationReached = when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.getPrev() == null
                    LoadType.REFRESH,
                    LoadType.APPEND -> response.pagingLinks?.getNext() == null
                },
            ))
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}
