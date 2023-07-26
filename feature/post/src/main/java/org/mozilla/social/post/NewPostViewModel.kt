package org.mozilla.social.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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
import org.mozilla.social.model.ImageState
import org.mozilla.social.model.entity.PollOption
import org.mozilla.social.model.entity.StatusVisibility
import org.mozilla.social.model.entity.request.PollCreate
import org.mozilla.social.post.interactions.ImageInteractions
import org.mozilla.social.post.interactions.PollInteractions
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.poll.PollStyle
import java.io.File

class NewPostViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
    private val log: Log,
    private val onStatusPosted: () -> Unit,
    private val pollDelegate: PollDelegate = PollDelegate()
) : ViewModel(),
    ImageInteractions,
    PollInteractions by pollDelegate
{
    val poll = pollDelegate.poll

    private val _statusText = MutableStateFlow("")
    val statusText = _statusText.asStateFlow()

    private val _imageStates = MutableStateFlow<Map<Uri, ImageState>>(emptyMap())
    val imageStates = _imageStates.asStateFlow()

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusText, imageStates, poll) { statusText, imageStates, poll ->
            (statusText.isNotBlank() || imageStates.isNotEmpty())
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

    private val uploadJobs = mutableMapOf<Uri, Job>()

    fun onStatusTextUpdated(text: String) {
        if (text.length > MAX_POST_LENGTH) return
        _statusText.update { text }
    }

    override fun onImageDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) {
        if (text.length > MAX_POST_LENGTH) return
        updateImageState(uri, description = text)
    }

    override fun onDeleteImageClicked(uri: Uri) {
        uploadJobs[uri]?.cancel()
        _imageStates.update {
            _imageStates.value.toMutableMap().apply { remove(uri) }
        }
    }

    /**
     * When an image is inserted, we need to upload it and hold onto the attachment id we get
     * from the server.
     */
    override fun onImageInserted(
        uri: Uri,
        file: File,
    ) {
        // if the image was already uploaded, just return
        imageStates.value[uri]?.let {
            if (it.loadState == LoadState.LOADED) {
                return
            }
        }
        updateImageState(uri, loadState = LoadState.LOADING)
        uploadJobs[uri] = viewModelScope.launch {
            try {
                val imageId = mediaRepository.uploadImage(
                    file,
                    imageStates.value[uri]?.description?.ifBlank { null }
                ).attachmentId
                updateImageState(
                    uri,
                    loadState = LoadState.LOADED,
                    attachmentId = imageId
                )
            } catch (e: Exception) {
                log.e(e)
                updateImageState(uri, loadState = LoadState.ERROR)
            }
        }
        uploadJobs[uri]?.invokeOnCompletion {
            log.d("removed")
            uploadJobs.remove(uri)
        }
    }

    fun onVisibilitySelected(statusVisibility: StatusVisibility) {
        _visibility.update { statusVisibility }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            _isSendingPost.update { true }
            try {
                statusRepository.sendPost(
                    statusText = statusText.value,
                    imageStates = imageStates.value.values.toList(),
                    visibility = visibility.value,
                    pollCreate = poll.value?.let { poll ->
                        PollCreate(
                            options = poll.options.map { PollOption(it) },
                            expiresInSec = poll.pollDuration.inSeconds,
                            allowMultipleChoices = poll.style == PollStyle.MULTIPLE_CHOICE,
                            hideTotals = poll.hideTotals
                        )
                    }
                )
                onStatusPosted()
            } catch (e: Exception) {
                log.e(e)
                _errorToastMessage.emit("Error Sending Post")
                _isSendingPost.update { false }
            }
        }
    }

    private fun updateImageState(
        uri: Uri,
        loadState: LoadState? = null,
        attachmentId: String? = null,
        description: String? = null,
    ) {
        val oldState = _imageStates.value[uri]
        val newState = ImageState(
            loadState = loadState ?: oldState?.loadState ?: LoadState.LOADING,
            attachmentId = attachmentId ?: oldState?.attachmentId,
            description = description ?: oldState?.description ?: "",
        )

        _imageStates.update {
            _imageStates.value.toMutableMap().apply {
                put(uri, newState)
            }
        }
    }

    companion object {
        /**
         * The maximum number of images allowed to be attached to a single post.
         * This number is defined by the mastodon API
         */
        const val MAX_IMAGES = 4
        const val MAX_POST_LENGTH = 500
    }
}
