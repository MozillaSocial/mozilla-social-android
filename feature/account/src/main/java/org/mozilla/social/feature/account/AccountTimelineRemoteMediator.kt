package org.mozilla.social.feature.account

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshAccountTimeline
import org.mozilla.social.core.usecase.mastodon.timeline.TimelineType

@OptIn(ExperimentalPagingApi::class)
class AccountTimelineRemoteMediator(
    private val accountId: String,
    private val timelineType: StateFlow<TimelineType>,
) :
    RemoteMediator<Int, AccountTimelineStatusWrapper>() {

    private val refreshAccountTimeline: RefreshAccountTimeline by inject(
        RefreshAccountTimeline::class.java
    ) {
        parametersOf(
            accountId,
            timelineType,
        )
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AccountTimelineStatusWrapper>
    ): MediatorResult {
        return refreshAccountTimeline(loadType, state)
    }
}

