package org.mozilla.social.model

import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.FileType

data class ImageState(
    val loadState: LoadState = LoadState.LOADING,
    val fileType: FileType,
    val attachmentId: String? = null,
    val description: String = "",
)