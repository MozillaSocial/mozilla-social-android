package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper

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
