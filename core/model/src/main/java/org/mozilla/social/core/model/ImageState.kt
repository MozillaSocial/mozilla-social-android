package org.mozilla.social.core.model

import android.net.Uri
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.FileType

data class ImageState(
    val uri: Uri,
    val loadState: LoadState = LoadState.LOADING,
    val fileType: FileType,
    val attachmentId: String? = null,
    val description: String = "",
)