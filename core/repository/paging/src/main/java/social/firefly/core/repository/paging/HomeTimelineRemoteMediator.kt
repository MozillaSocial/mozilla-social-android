package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper

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
