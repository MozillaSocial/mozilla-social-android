package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import social.firefly.common.Rel
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.model.paging.StatusPagingWrapper
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineRemoteMediator(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
    private val userPreferencesDatastore: UserPreferencesDatastore,
) : RemoteMediator<Int, HomeTimelineStatusWrapper>() {

    private var firstLoad = true

    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HomeTimelineStatusWrapper>,
    ): MediatorResult {
        return try {
            val pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        fetchRefresh(state)
                    }

                    LoadType.PREPEND -> {
                        val firstItem =
                            state.firstItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        timelineRepository.getHomeTimeline(
                            olderThanId = null,
                            immediatelyNewerThanId = firstItem.homeTimelineStatus.statusId,
                            loadSize = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        timelineRepository.getHomeTimeline(
                            olderThanId = lastItem.homeTimelineStatus.statusId,
                            immediatelyNewerThanId = null,
                            loadSize = pageSize,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.statuses)

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    timelineRepository.deleteHomeTimeline()
                }

                saveStatusToDatabase(result)
                timelineRepository.insertAllIntoHomeTimeline(result)
            }

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(200)
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

    private suspend fun fetchRefresh(
        state: PagingState<Int, HomeTimelineStatusWrapper>,
    ): StatusPagingWrapper {
        var olderThanId: String? = null
        val pageSize = state.config.initialLoadSize

        // If this is the first time we are loading the page, we need to start where
        // the user last left off.  Grab the lastSeenHomeStatusId
        if (firstLoad) {
            firstLoad = false
            val lastSeenId = CompletableDeferred<String>()
            with(CoroutineScope(coroutineContext)) {
                launch {
                    userPreferencesDatastore.lastSeenHomeStatusId.collectLatest {
                        lastSeenId.complete(it)
                        cancel()
                    }
                }
            }
            olderThanId = lastSeenId.await()
        }

        val mainResponse = timelineRepository.getHomeTimeline(
            olderThanId = olderThanId,
            immediatelyNewerThanId = null,
            loadSize = if (olderThanId != null) {
                // if we are going to fetch the first status separately, we need to decrease this
                // call's page size by 1
                pageSize - 1
            } else {
                pageSize
            },
        )

        val firstIdInMainList =
            mainResponse.statuses.maxByOrNull { it.statusId }?.statusId

        val topStatusResponse = if (olderThanId != null) {
            timelineRepository.getHomeTimeline(
                olderThanId = null,
                immediatelyNewerThanId = firstIdInMainList,
                loadSize = 1,
            )
        } else {
            null
        }

        return StatusPagingWrapper(
            statuses = buildList {
                addAll(mainResponse.statuses)
                topStatusResponse?.let { addAll(it.statuses) }
            },
            pagingLinks = buildList {
                mainResponse.pagingLinks?.find { it.rel == Rel.NEXT }?.let {
                    add(it)
                }
                if (topStatusResponse != null) {
                    topStatusResponse.pagingLinks?.find { it.rel == Rel.PREV }?.let {
                        add(it)
                    }
                } else {
                    mainResponse.pagingLinks?.find { it.rel == Rel.PREV }?.let {
                        add(it)
                    }
                }
            }
        )
    }
}
