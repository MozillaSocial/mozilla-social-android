package org.mozilla.social.feature.hashtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.remotemediators.HashTagTimelineRemoteMediator

class HashTagViewModel(
    private val analytics: Analytics,
    timelineRepository: TimelineRepository,
    hashTag: String,
    userAccountId: GetLoggedInUserAccountId,
) : ViewModel(), HashTagInteractions {
    private val hashTagTimelineRemoteMediator: HashTagTimelineRemoteMediator by inject(
        HashTagTimelineRemoteMediator::class.java,
    ) { parametersOf(hashTag) }

    @OptIn(ExperimentalPagingApi::class)
    val feed = timelineRepository.getHashtagTimelinePager(
        hashTag = hashTag,
        remoteMediator = hashTagTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId())
        }
    }.cachedIn(viewModelScope)

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.HASHTAG_SCREEN_IMPRESSION,
        )
    }

    val postCardDelegate: PostCardDelegate by inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope) }
}
