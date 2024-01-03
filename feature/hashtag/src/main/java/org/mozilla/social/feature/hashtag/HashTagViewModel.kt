package org.mozilla.social.feature.hashtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.repository.paging.HashTagTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.hashtag.FollowHashTag
import org.mozilla.social.core.usecase.mastodon.hashtag.UnfollowHashTag
import timber.log.Timber

class HashTagViewModel(
    private val analytics: Analytics,
    timelineRepository: TimelineRepository,
    hashTag: String,
    userAccountId: GetLoggedInUserAccountId,
    private val unfollowHashTag: UnfollowHashTag,
    private val followHashTag: FollowHashTag,
) : ViewModel(), HashTagInteractions, KoinComponent {

    private val hashTagTimelineRemoteMediator: HashTagTimelineRemoteMediator by inject {
        parametersOf(hashTag)
    }

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(viewModelScope, AnalyticsIdentifiers.FEED_PREFIX_HASHTAG)
    }

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

    override fun onFollowClicked(name: String, isFollowing: Boolean) {
        viewModelScope.launch {
            if (isFollowing) {
                try {
                    unfollowHashTag(name)
                } catch (e: UnfollowHashTag.UnfollowFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    followHashTag(name)
                } catch (e: FollowHashTag.FollowFailedException) {
                    Timber.e(e)
                }
            }
        }
    }
}
