package org.mozilla.social.post

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

    private val attachmentId = MutableStateFlow<String?>(null)

    val sendButtonEnabled: StateFlow<Boolean> =
        combine(statusText, attachmentId) { statusText, attachmentId ->
            statusText.isNotBlank() || !attachmentId.isNullOrBlank()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    private val _imageState = MutableStateFlow<LoadState>(LoadState.LOADING)
    val imageState: StateFlow<LoadState> = _imageState

    private val _imageDescription = MutableStateFlow("")
    val imageDescription: StateFlow<String> = _imageDescription

    fun onStatusTextUpdated(text: String) {
        _statusText.update { text }
    }

    fun onImageDescriptionTextUpdated(text: String) {
        _imageDescription.update { text }
    }

    fun onImageRemoved() {
        attachmentId.update { null }
    }

    fun onImageInserted(file: File) {
        _imageState.update { LoadState.LOADING }
        viewModelScope.launch {
            try {
                val imageId = mediaRepository.uploadImage(
                    file,
                    imageDescription.value.ifBlank { null }
                ).attachmentId
                attachmentId.update { imageId }
                _imageState.update { LoadState.LOADED }
            } catch (e: Exception) {
                log.e(e)
                _imageState.update { LoadState.ERROR }
            }
        }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            statusRepository.sendPost(
                statusText = statusText.value,
                attachmentId = attachmentId.value
            )
            onStatusPosted()
        }
    }
}
