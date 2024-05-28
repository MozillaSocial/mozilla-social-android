package social.firefly.core.repository.paging.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import social.firefly.common.Rel
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
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
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) : RemoteMediator<Int, HomeTimelineStatusWrapper>() {

    private var firstRefreshHasHappened = false

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
                            maxId = null,
                            minId = firstItem.homeTimelineStatus.statusId,
                            limit = pageSize,
                        )
                    }

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = true
                                )
                        timelineRepository.getHomeTimeline(
                            maxId = lastItem.homeTimelineStatus.statusId,
                            minId = null,
                            limit = pageSize,
                        )
                    }
                }

            val result = getInReplyToAccountNames(response.items)

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
                endOfPaginationReached = when (loadType) {
                    LoadType.PREPEND -> response.pagingLinks?.find { it.rel == Rel.PREV } == null
                    LoadType.REFRESH -> response.pagingLinks.isNullOrEmpty()
                    LoadType.APPEND -> response.pagingLinks?.find { it.rel == Rel.NEXT } == null
                },
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchRefresh(
        state: PagingState<Int, HomeTimelineStatusWrapper>,
    ): MastodonPagedResponse<Status> {
        var olderThanId: String? = null
        val pageSize = state.config.initialLoadSize

        // If this is the first time we are loading the page, we need to start where
        // the user last left off.  Grab the lastSeenHomeStatusId
        if (!firstRefreshHasHappened) {
            val lastSeenId = CompletableDeferred<String>()
            with(CoroutineScope(coroutineContext)) {
                launch {
                    userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
                        it.lastSeenHomeStatusId
                    }.collectLatest {
                        lastSeenId.complete(it)
                        cancel()
                    }
                }
            }
            olderThanId = lastSeenId.await()
        }

        val mainResponse = timelineRepository.getHomeTimeline(
            maxId = olderThanId,
            minId = null,
            limit = if (olderThanId != null) {
                // if we are going to fetch the first status separately, we need to decrease this
                // call's page size by 1
                pageSize - 1
            } else {
                pageSize
            },
        )

        val firstIdInMainList =
            mainResponse.items.maxByOrNull { it.statusId }?.statusId

        val topStatusResponse = if (olderThanId != null) {
            timelineRepository.getHomeTimeline(
                maxId = null,
                minId = firstIdInMainList,
                limit = 1,
            )
        } else {
            null
        }

        firstRefreshHasHappened = true

        return MastodonPagedResponse(
            items = buildList {
                addAll(mainResponse.items)
                topStatusResponse?.let { addAll(it.items) }
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
