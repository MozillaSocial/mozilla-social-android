package org.mozilla.social.post.media

import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.core.repository.mastodon.MediaRepository
import org.mozilla.social.model.ImageState
import org.mozilla.social.post.NewPostViewModel
import timber.log.Timber
import java.io.File

class MediaDelegate(
    private val coroutineScope: CoroutineScope,
    private val mediaRepository: MediaRepository,
) : MediaInteractions {

    private val _imageStates = MutableStateFlow<List<ImageState>>(emptyList())
    val imageStates: StateFlow<List<ImageState>> = _imageStates.asStateFlow()

    private val uploadJobs = mutableMapOf<Uri, Job>()

    override fun onMediaDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) {
        if (text.length > NewPostViewModel.MAX_IMAGE_DESCRIPTION_LENGTH) return

        updateImageState(uri = uri) { copy(description = text) }
    }

    override fun onDeleteMediaClicked(uri: Uri) {
        uploadJobs[uri]?.cancel()
        _imageStates.update {
            _imageStates.value.toMutableList().apply { removeIf { it.uri == uri } }
        }
    }

    /**
     * When an image is inserted, we need to upload it and hold onto the attachment id we get
     * from the server.
     */
    override fun onMediaInserted(
        uri: Uri,
        file: File,
        fileType: FileType,
    ) {
        // if the image was already uploaded, just return
        imageStates.value.firstOrNull { it.uri == uri }
            ?.let {
                if (it.loadState == LoadState.LOADED) {
                    return
                }
            }
        _imageStates.update {
            it.toMutableList()
                .apply { add(ImageState(uri, loadState = LoadState.LOADING, fileType = fileType)) }
        }
        updateImageState(uri) { copy(loadState = LoadState.LOADING) }
        uploadJobs[uri] = coroutineScope.launch {
            try {
                val imageId = mediaRepository.uploadImage(
                    file = file,
                    description = imageStates.value.firstOrNull { it.uri == uri }
                        ?.description?.ifBlank { null }
                ).attachmentId
                updateImageState(uri) {
                    copy(
                        attachmentId = imageId,
                        loadState = LoadState.LOADED
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                updateImageState(uri) { copy(loadState = LoadState.ERROR) }
            }
        }
        uploadJobs[uri]?.invokeOnCompletion {
            Timber.d("removed")
            uploadJobs.remove(uri)
        }
    }

    /**
     * Updates the state with the given uri with the given transform function
     */
    private inline fun updateImageState(uri: Uri, transform: ImageState.() -> ImageState) {
        _imageStates.update {
            it.toMutableList().apply {
                val oldState = firstOrNull { it.uri == uri }
                    ?: error("The media isn't in the list yet")
                this[this.indexOf(oldState)] = transform(oldState)
            }
        }
    }
}