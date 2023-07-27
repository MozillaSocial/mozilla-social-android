package org.mozilla.social.post.media

import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.model.ImageState
import org.mozilla.social.post.NewPostViewModel
import java.io.File

class MediaDelegate(
    private val mediaRepository: MediaRepository,
    private val log: Log,
) : MediaInteractions {

    lateinit var coroutineScope: CoroutineScope

    private val _imageStates = MutableStateFlow<Map<Uri, ImageState>>(emptyMap())
    val imageStates = _imageStates.asStateFlow()

    private val uploadJobs = mutableMapOf<Uri, Job>()

    override fun onMediaDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) {
        if (text.length > NewPostViewModel.MAX_IMAGE_DESCRIPTION_LENGTH) return
        updateImageState(uri, description = text)
    }

    override fun onDeleteMediaClicked(uri: Uri) {
        uploadJobs[uri]?.cancel()
        _imageStates.update {
            _imageStates.value.toMutableMap().apply { remove(uri) }
        }
    }

    /**
     * When an image is inserted, we need to upload it and hold onto the attachment id we get
     * from the server.
     */
    override fun onMediaInserted(
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
        uploadJobs[uri] = coroutineScope.launch {
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
}