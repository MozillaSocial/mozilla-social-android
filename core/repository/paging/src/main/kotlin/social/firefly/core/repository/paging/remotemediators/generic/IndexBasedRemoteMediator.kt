package social.firefly.core.repository.paging.remotemediators.generic

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.core.model.PageItem
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class IndexBasedRemoteMediator<T : Any, DBO : Any>(
    private val saveLocally: suspend (
        items: List<PageItem<T>>,
        isRefresh: Boolean,
    ) -> Unit,
    private val getRemotely: suspend (
        limit: Int,
        offset: Int,
    ) -> List<T>
) : RemoteMediator<Int, DBO>() {

    private var nextPositionIndex = 0

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBO>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val currentPage: List<PageItem<T>> =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize

                        getRemotely(
                            pageSize,
                            0,
                        ).mapIndexed { index, dbo ->
                            PageItem(position = index + nextPositionIndex, item = dbo)
                        }
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        getRemotely(
                            pageSize,
                            nextPositionIndex,
                        ).mapIndexed { index, dbo ->
                            PageItem(position = index + nextPositionIndex, item = dbo)
                        }
                    }
                }

            if (loadType == LoadType.REFRESH) {
                nextPositionIndex = 0
            }

            saveLocally(currentPage, loadType == LoadType.REFRESH)

            nextPositionIndex += currentPage.size

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

            (MediatorResult.Success(
                endOfPaginationReached = when (loadType) {
                    LoadType.REFRESH,
                    LoadType.APPEND -> currentPage.isEmpty()

                    else -> true
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
