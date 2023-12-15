package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper

@OptIn(ExperimentalPagingApi::class)
class LocalTimelineRemoteMediator(
    private val refreshLocalTimeline: RefreshLocalTimeline,
) : RemoteMediator<Int, LocalTimelineStatusWrapper>() {
    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalTimelineStatusWrapper>,
    ): MediatorResult {
        return refreshLocalTimeline(loadType = loadType, state = state)
    }
}
