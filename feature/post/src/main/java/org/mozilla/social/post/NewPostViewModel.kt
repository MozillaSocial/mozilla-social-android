package org.mozilla.social.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.model.ImageState
import java.io.File

class NewPostViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
    private val log: Log,
    private val onStatusPosted: () -> Unit,
) : ViewModel() {

    private val _statusText = MutableStateFlow("")
    val statusText: StateFlow<String> = _statusText

    private val _imageStates = MutableStateFlow<Map<Uri, ImageState>>(emptyMap())
    val imageStates: StateFlow<Map<Uri, ImageState>> = _imageStates

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusText, imageStates) { statusText, imageStates ->
            statusText.isNotBlank() || imageStates.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    val addImageButtonEnabled : StateFlow<Boolean> =
        imageStates.map {
            it.size < 4
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    fun onStatusTextUpdated(text: String) {
        _statusText.update { text }
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

    fun onImageDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) {
        updateImageState(uri, description = text)
    }

    fun onImageRemoved(uri: Uri) {
        _imageStates.update {
            _imageStates.value.toMutableMap().apply { remove(uri) }
        }
    }

    /**
     * When an image is inserted, we need to upload it and hold onto the attachment id we get
     * from the server.
     */
    fun onImageInserted(
        uri: Uri,
        file: File,
    ) {
        updateImageState(uri, loadState = LoadState.LOADING)
        viewModelScope.launch {
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
    }

    fun onPostClicked() {
        viewModelScope.launch {
            statusRepository.sendPost(
                statusText = statusText.value,
                imageStates = imageStates.value.values.toList()
            )
            onStatusPosted()
        }
    }
}
