package social.firefly.core.ui.postcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.TransparentNoTouchOverlay
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.postcard.components.Avatar
import social.firefly.core.ui.postcard.components.BottomRow
import social.firefly.core.ui.postcard.components.DepthLines
import social.firefly.core.ui.postcard.components.MetaData
import social.firefly.core.ui.postcard.components.PostContent
import social.firefly.core.ui.postcard.components.TopRowMetaData

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: PostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    NoRipple {
        Box(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                if (post.depthLinesUiState != null) {
                    DepthLines(
                        depthLinesUiState = post.depthLinesUiState,
                    )
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Column(
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp, top = 8.dp)
                        .clickable {
                            if (post.isClickable) {
                                postCardInteractions.onPostCardClicked(post.mainPostCardUiState.statusId)
                            }
                        },
                ) {
                    post.topRowMetaDataUiState?.let {
                        TopRowMetaData(
                            topRowMetaDataUiState = it,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Post(
                        post = post.mainPostCardUiState,
                        showViewMoreReplies = post.depthLinesUiState?.showViewMoreRepliesText ?: false,
                        expandRepliesButtonUiState = post.depthLinesUiState?.expandRepliesButtonUiState
                            ?: ExpandRepliesButtonUiState.HIDDEN,
                        postCardInteractions = postCardInteractions,
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = post.mainPostCardUiState.isBeingDeleted,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                TransparentNoTouchOverlay()
            }
        }
    }
}

@Composable
private fun Post(
    post: MainPostCardUiState,
    showViewMoreReplies: Boolean,
    expandRepliesButtonUiState: ExpandRepliesButtonUiState,
    postCardInteractions: PostCardInteractions,
) {
    Row {
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Avatar(
                post = post,
                postCardInteractions = postCardInteractions,
            )

            if (expandRepliesButtonUiState != ExpandRepliesButtonUiState.HIDDEN) {
                IconButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(20.dp)
                        .align(Alignment.BottomStart)
                        .offset(
                            x = (-9).dp
                        ),
                    onClick = { postCardInteractions.onHideRepliesClicked(post.statusId) }
                ) {
                    Icon(
                        modifier = Modifier
                            .background(FfTheme.colors.layer1),
                        painter = if (expandRepliesButtonUiState == ExpandRepliesButtonUiState.PLUS) {
                            FfIcons.plusCircle()
                        } else {
                            FfIcons.minusCircle()
                        },
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            MetaData(
                post = post,
            )

            PostContent(
                uiState = post.postContentUiState,
                postCardInteractions = postCardInteractions,
            )

            Box(
                modifier = Modifier.height(36.dp)
            ) {
                BottomRow(
                    post = post,
                    postCardInteractions = postCardInteractions,
                )
            }

            if (showViewMoreReplies) {
                MediumTextLabel(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    text = stringResource(id = R.string.view_more_replies)
                )
            }
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreview() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = null,
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardWithContentWarningPreview() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "Micky mouse spoilers!",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = null,
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithDepth() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    showViewMoreRepliesText = true,
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.HIDDEN,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithHighDepth() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 15,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.HIDDEN,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithMinusButton() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.MINUS,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithPlusButton() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.PLUS,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}
