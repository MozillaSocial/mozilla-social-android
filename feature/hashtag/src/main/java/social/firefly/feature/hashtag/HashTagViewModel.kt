package social.firefly.feature.hashtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.HashtagAnalytics
import social.firefly.core.model.HashTag
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.paging.remotemediators.HashTagTimelineRemoteMediator
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.hashtag.FollowHashTag
import social.firefly.core.usecase.mastodon.hashtag.GetHashTag
import social.firefly.core.usecase.mastodon.hashtag.UnfollowHashTag
import timber.log.Timber

class HashTagViewModel(
    private val analytics: HashtagAnalytics,
    timelineRepository: TimelineRepository,
    private val hashTag: String,
    userAccountId: GetLoggedInUserAccountId,
    private val unfollowHashTag: UnfollowHashTag,
    private val followHashTag: FollowHashTag,
    private val getHashTag: GetHashTag,
) : ViewModel(), HashTagInteractions, KoinComponent {

    private val hashTagTimelineRemoteMediator: HashTagTimelineRemoteMediator by inject {
        parametersOf(hashTag)
    }

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(FeedLocation.HASHTAG)
    }

    private var getHashTagJob: Job? = null

    private val _uiState = MutableStateFlow<Resource<HashTag>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val feed = timelineRepository.getHashtagTimelinePager(
        hashTag = hashTag,
        remoteMediator = hashTagTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId())
        }
    }.cachedIn(viewModelScope)

    init {
        loadHashTag()
    }

    private fun loadHashTag() {
        getHashTagJob?.cancel()
        getHashTagJob = viewModelScope.launch {
            getHashTag(
                name = hashTag,
                coroutineScope = viewModelScope,
            ).collect { resource ->
                _uiState.update { resource }
            }
        }
    }

    override fun onScreenViewed() {
        analytics.hashtagScreenViewed()
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
        analytics.followClicked()
    }

    override fun onRetryClicked() {
        loadHashTag()
    }
}
