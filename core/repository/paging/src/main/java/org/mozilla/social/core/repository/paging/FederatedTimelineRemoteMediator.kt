package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.mozilla.social.core.database.model.entities.statusCollections.FederatedTimelineStatusWrapper

@OptIn(ExperimentalPagingApi::class)
class FederatedTimelineRemoteMediator(
    private val refreshFederatedTimeline: RefreshFederatedTimeline,
) : RemoteMediator<Int, FederatedTimelineStatusWrapper>() {
    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FederatedTimelineStatusWrapper>,
    ): MediatorResult {
        return refreshFederatedTimeline(loadType = loadType, state = state)
    }
}
