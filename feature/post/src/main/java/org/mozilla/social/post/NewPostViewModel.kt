package org.mozilla.social.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.loadResource
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.analytics.EngagementType
import org.mozilla.social.core.model.ImageState
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.request.PollCreate
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.MediaRepository
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaDelegate
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.status.ContentWarningInteractions
import org.mozilla.social.post.status.StatusDelegate
import org.mozilla.social.post.status.StatusInteractions
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class NewPostViewModel(
    private val analytics: Analytics,
    private val replyStatusId: String?,
    mediaRepository: MediaRepository,
    searchRepository: SearchRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
    private val statusRepository: StatusRepository,
    private val postStatus: PostStatus,
    private val popNavBackstack: PopNavBackstack,
    private val showSnackbar: ShowSnackbar,
) : ViewModel(), NewPostInteractions {
    private val statusDelegate: StatusDelegate =
        StatusDelegate(
            viewModelScope,
            searchRepository,
            statusRepository,
            replyStatusId,
            analytics,
        )
    val statusInteractions: StatusInteractions = statusDelegate
    val contentWarningInteractions: ContentWarningInteractions = statusDelegate
    val statusUiState = statusDelegate.uiState

    private val pollDelegate: PollDelegate = PollDelegate(analytics)
    val pollInteractions: PollInteractions = pollDelegate
    val poll = pollDelegate.poll

    private val mediaDelegate: MediaDelegate =
        MediaDelegate(
            viewModelScope,
            mediaRepository,
        )
    val mediaInteractions: MediaInteractions = mediaDelegate
    val mediaStates: StateFlow<List<ImageState>> = mediaDelegate.imageStates

    private val images = mediaStates.mapLatest { imageStates ->
        imageStates.filter { imageState ->
            imageState.fileType == FileType.IMAGE
        }
    }
    private val videos = mediaStates.mapLatest { imageStates ->
        imageStates.filter { imageState ->
            imageState.fileType == FileType.VIDEO
        } 
    }

    val bottomBarState: StateFlow<BottomBarState> =
        combine(
            images,
            videos,
            poll,
            statusUiState,
        ) { images, videos, poll, statusUiState ->
            BottomBarState(
                imageButtonEnabled = videos.isEmpty() && images.size < MAX_IMAGES && poll == null,
                videoButtonEnabled = images.isEmpty() && poll == null,
                pollButtonEnabled = images.isEmpty() && videos.isEmpty() && poll == null,
                contentWarningText = statusUiState.contentWarningText,
                characterCountText = "${MAX_POST_LENGTH - 
                        statusUiState.statusText.text.length - 
                        (statusUiState.contentWarningText?.length ?: 0)}",
                maxImages = MAX_IMAGES - images.size,
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, BottomBarState())

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusUiState, mediaStates, poll) { statusUiState, imageStates, poll ->
            (statusUiState.statusText.text.isNotBlank() || imageStates.isNotEmpty()) &&
                // all images are loaded
                imageStates.find { it.loadState != LoadState.LOADED } == null &&
                // poll options have text if they exist
                poll?.options?.find { it.isBlank() } == null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    private val _isSendingPost = MutableStateFlow(false)
    val isSendingPost = _isSendingPost.asStateFlow()

    private val _visibility = MutableStateFlow(StatusVisibility.Public)
    val visibility = _visibility.asStateFlow()

    val userHeaderState: Flow<UserHeaderState> =
        loadResource {
            accountRepository.getAccount(getLoggedInUserAccountId())
        }.mapNotNull { resource ->
            resource.data?.let { account ->
                UserHeaderState(avatarUrl = account.avatarUrl, displayName = account.displayName)
            }
        }

    fun onVisibilitySelected(statusVisibility: StatusVisibility) {
        _visibility.update { statusVisibility }
    }

    fun onPostClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = AnalyticsIdentifiers.NEW_POST_POST,
        )
        viewModelScope.launch {
            _isSendingPost.update { true }
            try {
                postStatus(
                    statusText = statusUiState.value.statusText.text,
                    imageStates = mediaStates.value.toList(),
                    visibility = visibility.value,
                    pollCreate =
                        poll.value?.let { poll ->
                            PollCreate(
                                options = poll.options,
                                expiresInSec = poll.pollDuration.inSeconds,
                                allowMultipleChoices = poll.style == PollStyle.MULTIPLE_CHOICE,
                                hideTotals = poll.hideTotals,
                            )
                        },
                    contentWarningText = statusUiState.value.contentWarningText,
                    inReplyToId = replyStatusId,
                )

                onStatusPosted()
            } catch (e: Exception) {
                Timber.e(e)
                _isSendingPost.update { false }
            }
        }
    }

    private fun onStatusPosted() {
        showSnackbar(
            text = StringFactory.resource(R.string.your_post_was_published),
            isError = false,
        )
        popNavBackstack()
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.NEW_POST_SCREEN_IMPRESSION,
        )
    }

    override fun onUploadImageClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = AnalyticsIdentifiers.NEW_POST_IMAGE
        )
    }

    override fun onUploadMediaClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = AnalyticsIdentifiers.NEW_POST_MEDIA
        )
    }

    companion object {
        /**
         * The maximum number of images allowed to be attached to a single post.
         * This number is defined by the mastodon API
         */
        const val MAX_IMAGES = 4
        const val MAX_POST_LENGTH = 500
        const val MAX_POLL_COUNT = 4
        const val MIN_POLL_COUNT = 2
        const val MAX_IMAGE_DESCRIPTION_LENGTH = 1_500
    }
}
