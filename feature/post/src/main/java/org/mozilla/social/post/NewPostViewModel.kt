package org.mozilla.social.post

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.model.entity.StatusVisibility
import org.mozilla.social.model.entity.request.PollCreate
import org.mozilla.social.post.contentwarning.ContentWarningDelegate
import org.mozilla.social.post.contentwarning.ContentWarningInteractions
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.media.MediaDelegate
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.poll.PollStyle

class NewPostViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
    private val log: Log,
    private val onStatusPosted: () -> Unit,
    private val pollDelegate: PollDelegate = PollDelegate(),
    private val contentWarningDelegate: ContentWarningDelegate = ContentWarningDelegate(),
    private val mediaDelegate: MediaDelegate = MediaDelegate(
        mediaRepository,
        log,
    ),
) : ViewModel(),
    MediaInteractions by mediaDelegate,
    PollInteractions by pollDelegate,
    ContentWarningInteractions by contentWarningDelegate
{
    init {
        mediaDelegate.coroutineScope = viewModelScope
    }

    val poll = pollDelegate.poll
    val contentWarningText = contentWarningDelegate.contentWarningText
    val imageStates = mediaDelegate.imageStates

    private val _statusText = MutableStateFlow(TextFieldValue(""))
    val statusText = _statusText.asStateFlow()

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusText, imageStates, poll) { statusText, imageStates, poll ->
            (statusText.text.isNotBlank() || imageStates.isNotEmpty())
                    // all images are loaded
                    && imageStates.values.find { it.loadState != LoadState.LOADED } == null
                    // poll options have text if they exist
                    && poll?.options?.find { it.isBlank() } == null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    val addImageButtonEnabled : StateFlow<Boolean> =
        combine(imageStates, poll) { imageStates, poll ->
            imageStates.size < MAX_IMAGES && poll == null
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    val pollButtonEnabled : StateFlow<Boolean> =
        imageStates.map {
            it.isEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    private val _errorToastMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    private val _isSendingPost = MutableStateFlow(false)
    val isSendingPost = _isSendingPost.asStateFlow()

    private val _visibility = MutableStateFlow(StatusVisibility.Public)
    val visibility = _visibility.asStateFlow()

    fun onStatusTextUpdated(textFieldValue: TextFieldValue) {
        if (textFieldValue.text.length + (contentWarningText.value?.length ?: 0) > MAX_POST_LENGTH) return
        _statusText.update { textFieldValue }

    }

    override fun onContentWarningTextChanged(text: String) {
        if (text.length + statusText.value.text.length > MAX_POST_LENGTH) return
        contentWarningDelegate.onContentWarningTextChanged(text)
    }

    fun onVisibilitySelected(statusVisibility: StatusVisibility) {
        _visibility.update { statusVisibility }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            _isSendingPost.update { true }
            try {
                statusRepository.sendPost(
                    statusText = statusText.value.text,
                    imageStates = imageStates.value.values.toList(),
                    visibility = visibility.value,
                    pollCreate = poll.value?.let { poll ->
                        PollCreate(
                            options = poll.options,
                            expiresInSec = poll.pollDuration.inSeconds,
                            allowMultipleChoices = poll.style == PollStyle.MULTIPLE_CHOICE,
                            hideTotals = poll.hideTotals
                        )
                    },
                    contentWarningText = contentWarningText.value,
                )
                onStatusPosted()
            } catch (e: Exception) {
                log.e(e)
                _errorToastMessage.emit("Error Sending Post")
                _isSendingPost.update { false }
            }
        }
    }

    /**
     * @return the account string a user is typing if they are typing one, otherwise null
     */
    private fun accountText(textFieldValue: TextFieldValue): String? {
        if (!textFieldValue.text.contains("@")) return null
        val textBeforeCursor = textFieldValue.text.substring(0, textFieldValue.selection.end)
        val textBetweenSymbolAndCursor = textBeforeCursor.substringAfterLast('@')
        return if (!textBetweenSymbolAndCursor.contains(" ")) {
            textBetweenSymbolAndCursor
        } else {
            null
        }
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
