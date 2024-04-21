package social.firefly.core.ui.postcard

import social.firefly.common.utils.StringFactory
import social.firefly.core.model.Attachment
import social.firefly.core.model.Mention
import social.firefly.core.ui.poll.PollUiState

data class PostCardUiState(
    val statusId: String,
    val topRowMetaDataUiState: TopRowMetaDataUiState?,
    val mainPostCardUiState: MainPostCardUiState,
    val depthLinesUiState: DepthLinesUiState?,
    val isClickable: Boolean = true,
)

/**
 * @param isUsersPost true if the this status belongs to the logged in user
 */
data class MainPostCardUiState(
    val url: String?,
    val username: String,
    val profilePictureUrl: String,
    val postTimeSince: StringFactory,
    val accountName: StringFactory,
    val replyCount: String?,
    val boostCount: String?,
    val favoriteCount: String?,
    val statusId: String,
    val userBoosted: Boolean,
    val isFavorited: Boolean,
    val accountId: String,
    val isBeingDeleted: Boolean,
    val postContentUiState: PostContentUiState,
    val dropDownOptions: List<DropDownOption>
)

data class DropDownOption(
    val text: StringFactory,
    val onOptionClicked: () -> Unit,
)

data class PostContentUiState(
    val pollUiState: PollUiState?,
    val statusTextHtml: String,
    val mediaAttachments: List<Attachment>,
    val mentions: List<Mention>,
    val previewCard: PreviewCard?,
    val contentWarning: String,
    val onlyShowPreviewOfText: Boolean = false,
)

data class TopRowMetaDataUiState(
    val iconType: TopRowIconType,
    val text: StringFactory,
)

data class DepthLinesUiState(
    val postDepth: Int,
    val depthLines: List<Int>,
    val startingDepth: Int,
    val showViewMoreRepliesText: Boolean = false,
    val isHidingReplies: Boolean = false,
)

enum class TopRowIconType {
    REPLY,
    BOOSTED,
}

data class PreviewCard(
    val url: String,
    val title: String,
    val imageUrl: String,
    val providerName: String?,
)
