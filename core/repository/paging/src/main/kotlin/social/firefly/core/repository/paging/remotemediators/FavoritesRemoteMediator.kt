package social.firefly.core.repository.paging.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.Rel
import social.firefly.common.getMaxIdValue
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatusWrapper
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FavoritesRepository
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FavoritesRemoteMediator(
    private val favoritesRepository: FavoritesRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : RemoteMediator<Int, FavoritesTimelineStatusWrapper>() {
    private var nextKey: String? = null
    private var nextPositionIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FavoritesTimelineStatusWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        favoritesRepository.getFavorites(
                            maxId = null,
                            minId = null,
                            limit = pageSize,
                        )
                    }

                    LoadType.PREPEND -> {
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return MediatorResult.Success(endOfPaginationReached = true)
                        favoritesRepository.getFavorites(
                            maxId = null,
                            minId = firstItem.favoritesTimelineStatus.statusId,
                            limit = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(endOfPaginationReached = true)
                        favoritesRepository.getFavorites(
                            maxId = lastItem.favoritesTimelineStatus.statusId,
                            minId = null,
                            limit = pageSize,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.items)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    favoritesRepository.deleteFavoritesTimeline()
                    nextPositionIndex = 0
                }

                saveStatusToDatabase(result)

                favoritesRepository.insertAll(result.mapIndexed { index, status ->
                    FavoritesTimelineStatus(
                        statusId = status.statusId,
                        position = nextPositionIndex + index,
                    )
                })
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