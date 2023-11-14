package org.mozilla.social.feed.remoteMediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatusWrapper
import org.mozilla.social.core.usecase.mastodon.remotemediators.getInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshFederatedTimeline
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FederatedTimelineRemoteMediator(
    private val refreshFederatedTimeline: RefreshFederatedTimeline,
) : RemoteMediator<Int, FederatedTimelineStatusWrapper>() {

    @Suppress("ReturnCount", "MagicNumber")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FederatedTimelineStatusWrapper>
    ): MediatorResult {
        return refreshFederatedTimeline(loadType = loadType, state = state)
    }
}
