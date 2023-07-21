package org.mozilla.social.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.StatusRepository
import java.io.File

class NewPostViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
    private val log: Log,
    private val onStatusPosted: () -> Unit,
) : ViewModel() {

    private val _statusText = MutableStateFlow("")
    val statusText: StateFlow<String> = _statusText

    private val attachmentIds = MutableStateFlow<Map<Uri, String>>(emptyMap())

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusText, attachmentIds) { statusText, attachmentIds ->
            statusText.isNotBlank() || attachmentIds.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    private val _imageState = MutableStateFlow<Map<Uri, LoadState>>(emptyMap())
    val imageState: StateFlow<Map<Uri, LoadState>> = _imageState

    private val _imageDescriptions = MutableStateFlow<Map<Uri, String>>(emptyMap())
    val imageDescriptions: StateFlow<Map<Uri, String>> = _imageDescriptions

    fun onStatusTextUpdated(text: String) {
        _statusText.update { text }
    }

    fun onImageDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) {
        _imageDescriptions.update {
            _imageDescriptions.value.toMutableMap().apply { put(uri, text) }
        }
    }

    fun onImageRemoved(uri: Uri) {
        attachmentIds.update {
            attachmentIds.value.toMutableMap().apply { remove(uri) }
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
        _imageState.update {
            _imageState.value.toMutableMap().apply { put(uri, LoadState.LOADING) }
        }
        viewModelScope.launch {
            try {
                val imageId = mediaRepository.uploadImage(
                    file,
                    imageDescriptions.value[uri]?.ifBlank { null }
                ).attachmentId
                attachmentIds.update {
                    attachmentIds.value.toMutableMap().apply { put(uri, imageId) }
                }
                _imageState.update {
                    _imageState.value.toMutableMap().apply { put(uri, LoadState.LOADED) }
                }
            } catch (e: Exception) {
                log.e(e)
                _imageState.update {
                    _imageState.value.toMutableMap().apply { put(uri, LoadState.ERROR) }
                }
            }
        }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            statusRepository.sendPost(
                statusText = statusText.value,
                attachmentIds = attachmentIds.value.values.toList()
            )
            onStatusPosted()
        }
    }
}
