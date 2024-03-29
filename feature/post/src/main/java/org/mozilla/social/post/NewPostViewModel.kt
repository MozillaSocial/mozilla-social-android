package org.mozilla.social.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.loadResource
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.NewPostAnalytics
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.request.PollCreate
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.status.EditStatus
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaDelegate
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.status.StatusDelegate
import timber.log.Timber

class NewPostViewModel(
    private val analytics: NewPostAnalytics,
    private val replyStatusId: String?,
    private val editStatusId: String?,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
    private val postStatus: PostStatus,
    private val editStatus: EditStatus,
    private val popNavBackstack: PopNavBackstack,
    private val showSnackbar: ShowSnackbar,
    val pollDelegate: PollDelegate,
) : ViewModel(), NewPostInteractions, KoinComponent {

    val statusDelegate: StatusDelegate by inject {
        parametersOf(
            viewModelScope,
            replyStatusId,
            editStatusId,
        )
    }

    val mediaDelegate: MediaDelegate by inject {
        parametersOf(
            viewModelScope,
            editStatusId,
        )
    }

    private val _newPostUiState = MutableStateFlow(NewPostUiState())
    val newPostUiState = _newPostUiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                mediaDelegate.imageStates,
                pollDelegate.uiState,
                statusDelegate.uiState,
            ) { imagesStates, pollUiState, statusUiState ->
                val images = imagesStates.filter { it.fileType == FileType.IMAGE }
                val videos = imagesStates.filter { it.fileType == FileType.VIDEO }
                BottomBarState(
                    imageButtonEnabled = videos.isEmpty() && images.size < MAX_IMAGES && pollUiState == null,
                    videoButtonEnabled = images.isEmpty() && pollUiState == null,
                    pollButtonEnabled = images.isEmpty() && videos.isEmpty(),
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
        analytics.postClicked()
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

    override fun onEditClicked() {
        viewModelScope.launch {
            _newPostUiState.edit { copy(
                isSendingPost = true
            ) }
            try {
                if (editStatusId != null) {
                    editStatus(
                        statusId = editStatusId,
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
                }

                onStatusEdited()
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

    private fun onStatusEdited() {
        showSnackbar(
            text = StringFactory.resource(R.string.your_edit_was_published),
            isError = false,
        )
        popNavBackstack()
    }

    override fun onScreenViewed() {
        analytics.newPostScreenViewed()
    }

    override fun onUploadImageClicked() {
        analytics.uploadImageClicked()
    }

    override fun onUploadMediaClicked() {
        analytics.uploadMediaClicked()
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
