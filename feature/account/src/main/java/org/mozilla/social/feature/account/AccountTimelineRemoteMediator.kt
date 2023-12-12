package org.mozilla.social.feature.account

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.model.AccountTimelineType
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshAccountTimeline

@OptIn(ExperimentalPagingApi::class)
class AccountTimelineRemoteMediator(
    private val accountId: String,
    private val timelineType: AccountTimelineType,
) : RemoteMediator<Int, AccountTimelineStatusWrapper>(), KoinComponent {

    private val refreshAccountTimeline: RefreshAccountTimeline by inject {
        parametersOf(
            accountId,
            timelineType,
        )
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AccountTimelineStatusWrapper>,
    ): MediatorResult {
        return refreshAccountTimeline(loadType, state)
    }
}
