package org.mozilla.social.core.ui.postcard

import android.text.Spanned
import androidx.compose.ui.graphics.vector.ImageVector
import org.mozilla.social.core.ui.poll.PollUiState
import org.mozilla.social.model.Attachment

data class PostCardUiState(
    val statusId: String,
    val topRowMetaDataUiState: TopRowMetaDataUiState?,
    val mainPostCardUiState: MainPostCardUiState,
)

data class MainPostCardUiState(
    val url: String?,
    val pollUiState: PollUiState?,
    val username: String,
    val statusText: Spanned,
    val mediaAttachments: List<Attachment>,
    val profilePictureUrl: String,
    val postMetaDataText: String,
    val replyCount: Long,
    val boostCount: Long,
    val favoriteCount: Long,
    val statusId: String,
    val userBoosted: Boolean,
    val isFavorited: Boolean,
    val accountId: String,
)

data class TopRowMetaDataUiState(
    val icon: ImageVector,
    val text: String,
)