package social.firefly.core.repository.paging.remotemediators.generic

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.Rel
import social.firefly.common.getMaxIdValue
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class IdBasedRemoteMediator<T : Any, DBO : Any>(
    private val saveLocally: suspend (
        items: List<PageItem<T>>,
        isRefresh: Boolean,
    ) -> Unit,
    private val getRemotely: suspend (
        limit: Int,
        nextKey: String?,
    ) -> MastodonPagedResponse<T>
) : RemoteMediator<Int, DBO>() {

    private var nextKey: String? = null
    private var nextPositionIndex = 0

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBO>,
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    nextPositionIndex = 0

                    pageSize = state.config.initialLoadSize

                    getRemotely(
                        pageSize,
                        nextKey,
                    )
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    getRemotely(
                        pageSize,
                        nextKey,
                    )
                }
            }
            val currentPage: List<PageItem<T>> = response.items.mapIndexed { index, dbo ->
                PageItem(position = index + nextPositionIndex, item = dbo)
            }

            if (loadType == LoadType.REFRESH) {
                nextPositionIndex = 0
            }

            saveLocally(currentPage, loadType == LoadType.REFRESH)

            nextKey = response.pagingLinks?.getMaxIdValue()
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
                    LoadType.APPEND, -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
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