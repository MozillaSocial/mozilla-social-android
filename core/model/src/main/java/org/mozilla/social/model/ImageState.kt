package org.mozilla.social.model

import org.mozilla.social.common.LoadState

data class ImageState(
    val loadState: LoadState = LoadState.LOADING,
    val attachmentId: String? = null,
    val description: String = "",
)