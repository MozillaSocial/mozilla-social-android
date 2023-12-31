package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.mozilla.social.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineRemoteMediator(
    private val refreshHomeTimeline: RefreshHomeTimeline,
) : RemoteMediator<Int, HomeTimelineStatusWrapper>() {
    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HomeTimelineStatusWrapper>,
    ): MediatorResult {
        return refreshHomeTimeline(loadType = loadType, state = state)
    }
}
