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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.loadResource
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.analytics.EngagementType
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.request.PollCreate
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaDelegate
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.status.StatusDelegate
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class NewPostViewModel(
    private val analytics: Analytics,
    private val replyStatusId: String?,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
    private val postStatus: PostStatus,
    private val popNavBackstack: PopNavBackstack,
    private val showSnackbar: ShowSnackbar,
    val pollDelegate: PollDelegate,
) : ViewModel(), NewPostInteractions, KoinComponent {

    val statusDelegate: StatusDelegate by inject {
        parametersOf(
            viewModelScope,
            replyStatusId,
        )
    }

    val mediaDelegate: MediaDelegate by inject {
        parametersOf(
            viewModelScope,
        )
    }

    private val _newPostUiState = MutableStateFlow(NewPostUiState())
    val newPostUiState = _newPostUiState.asStateFlow()

    private val images = mediaDelegate.imageStates.mapLatest { imageStates ->
        imageStates.filter { imageState ->
            imageState.fileType == FileType.IMAGE
        }
    }
    private val videos = mediaDelegate.imageStates.mapLatest { imageStates ->
        imageStates.filter { imageState ->
            imageState.fileType == FileType.VIDEO
        }
    }

    init {
        viewModelScope.launch {
            combine(
                images,
                videos,
                pollDelegate.uiState,
                statusDelegate.uiState,
            ) { images, videos, pollUiState, statusUiState ->
                BottomBarState(
                    imageButtonEnabled = videos.isEmpty() && images.size < MAX_IMAGES && pollUiState == null,
                    videoButtonEnabled = images.isEmpty() && pollUiState == null,
                    pollButtonEnabled = images.isEmpty() && videos.isEmpty() && pollUiState == null,
                    contentWarningText = statusUiState.contentWarningText,
                    characterCountText = "${MAX_POST_LENGTH -
                            statusUiState.statusText.text.length -
                            (statusUiState.contentWarningText?.length ?: 0)}",
                    maxImages = MAX_IMAGES - images.size,
                )
            }.collect {
                _newPostUiState.edit { copy(
                    bottomBarState = it
                ) }
            }
        }

        viewModelScope.launch {
            combine(
                statusDelegate.uiState,
                mediaDelegate.imageStates,
                pollDelegate.uiState
            ) { statusUiState, imageStates, poll ->
                (statusUiState.statusText.text.isNotBlank() || imageStates.isNotEmpty()) &&
                        // all images are loaded
                        imageStates.find { it.loadState != LoadState.LOADED } == null &&
                        // poll options have text if they exist
                        poll?.options?.find { it.isBlank() } == null
            }.collect {
                _newPostUiState.edit { copy(
                    sendButtonEnabled = it
                ) }
            }
        }

        viewModelScope.launch {
            loadResource {
                accountRepository.getAccount(getLoggedInUserAccountId())
            }.mapNotNull { resource ->
                resource.data?.let { account ->
                    UserHeaderState(avatarUrl = account.avatarUrl, displayName = account.displayName)
                }
            }.collect {
                _newPostUiState.edit { copy(
                    userHeaderState = it
                ) }
            }
        }
    }

    override fun onVisibilitySelected(statusVisibility: StatusVisibility) {
        _newPostUiState.edit { copy(
            visibility = statusVisibility
        ) }
    }

    override fun onPostClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = AnalyticsIdentifiers.NEW_POST_POST,
        )
        viewModelScope.launch {
            _newPostUiState.edit { copy(
                isSendingPost = true
            ) }
            try {
                postStatus(
                    statusText = statusDelegate.uiState.value.statusText.text,
                    imageStates = mediaDelegate.imageStates.value.toList(),
                    visibility = newPostUiState.value.visibility,
                    pollCreate = pollDelegate.uiState.value?.let { poll ->
                        PollCreate(
                            options = poll.options,
                            expiresInSec = poll.pollDuration.inSeconds,
                            allowMultipleChoices = poll.style == PollStyle.MULTIPLE_CHOICE,
                            hideTotals = poll.hideTotals,
                        )
                    },
                    contentWarningText = statusDelegate.uiState.value.contentWarningText,
                    inReplyToId = replyStatusId,
                )

                onStatusPosted()
            } catch (e: Exception) {
                Timber.e(e)
                _newPostUiState.edit { copy(
                    isSendingPost = false
                ) }
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
