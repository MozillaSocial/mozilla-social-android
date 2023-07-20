package org.mozilla.social.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.StatusRepository
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

class NewPostViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
    private val onStatusPosted: () -> Unit,
) : ViewModel() {

    private val _statusText = MutableStateFlow("")
    val statusText: StateFlow<String> = _statusText

    private val _attachmentId = MutableStateFlow<String?>(null)
    val attachmentId: StateFlow<String?> = _attachmentId

    private val _sendButtonEnabled = MutableStateFlow(false).apply {
        viewModelScope.launch {
            combine(statusText, attachmentId) { statusText, attachmentId ->
                statusText.isNotBlank() || !attachmentId.isNullOrBlank()
            }.collect {
                value = it
            }
        }
    }
    val sendButtonEnabled: StateFlow<Boolean> = _sendButtonEnabled

    fun onStatusTextUpdated(text: String) {
        _statusText.update { text }
    }

    fun onImageInserted(inputStream: InputStream) {
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (true) {
            len = inputStream.read(buffer)
            if (len == -1) {
                break
            }
            out.write(buffer, 0, len)
        }
        val byteBuffer = ByteBuffer.wrap(out.toByteArray())
        inputStream.close()
        out.close()
        viewModelScope.launch {
            try {
                val imageId = mediaRepository.uploadImage(
                    "test",
                    byteBuffer
                ).attachmentId
                _attachmentId.update { imageId }
            } catch (e: Exception) {
                println(e.message)
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