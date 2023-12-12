package org.mozilla.social.feed.remoteMediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshLocalTimeline

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
