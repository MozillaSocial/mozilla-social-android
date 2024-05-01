package social.firefly.core.model

import android.net.Uri
import social.firefly.common.LoadState
import social.firefly.common.utils.FileType

data class ImageState(
    val uri: Uri,
    val loadState: LoadState = LoadState.LOADING,
    val fileType: FileType,
    val attachmentId: String? = null,
    val description: String = "",
)
