package org.mozilla.social.core.ui.postcard

import android.text.Spanned
import org.mozilla.social.core.ui.poll.PollUiState
import org.mozilla.social.model.Attachment

data class PostCardUiState(
    val pollUiState: PollUiState?,
    val topRowMetaDataText: String?,
    val showBoostedMetaData: Boolean,
    val showInReplyToMetaData: Boolean,
    val statusText: Spanned,
    val mediaAttachments: List<Attachment>,
    val profilePictureUrl: String,
    val postMetaDataText: String,
    val replyCount: Long,
    val boostCount: Long,
    val favoriteCount: Long,
)