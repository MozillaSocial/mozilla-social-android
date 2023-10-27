package org.mozilla.social.core.ui.postcard

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.poll.PollUiState
import org.mozilla.social.model.Attachment
import org.mozilla.social.model.Mention

data class PostCardUiState(
    val statusId: String,
    val topRowMetaDataUiState: TopRowMetaDataUiState?,
    val mainPostCardUiState: MainPostCardUiState,
)

data class MainPostCardUiState(
    val url: String?,
    val pollUiState: PollUiState?,
    val username: String,
    val statusTextHtml: String,
    val mediaAttachments: List<Attachment>,
    val profilePictureUrl: String,
    val postTimeSince: StringFactory,
    val accountName: StringFactory,
    val replyCount: Long,
    val boostCount: Long,
    val favoriteCount: Long,
    val statusId: String,
    val userBoosted: Boolean,
    val isFavorited: Boolean,
    val accountId: String,
    val mentions: List<Mention>,
    val previewCard: PreviewCard?,
)

data class TopRowMetaDataUiState(
    val iconType: TopRowIconType,
    val text: StringFactory,
)

enum class TopRowIconType {
    REPLY,
    BOOSTED,
}

data class PreviewCard(
    val url: String,
    val title: String,
    val imageUrl: String,
    val providerName: String?
)