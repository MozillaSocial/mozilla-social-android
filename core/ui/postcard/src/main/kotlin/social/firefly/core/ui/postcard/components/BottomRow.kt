package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.common.utils.toPxInt
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.getMaxWidth
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.OverflowDropDownType
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostContentUiState

@Suppress("MagicNumber")
@Composable
internal fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    val width = getMaxWidth()

    Layout(
        modifier = modifier,
        content = {
            BottomIconButton(
                onClick = { postCardInteractions.onReplyClicked(post.statusId) },
                painter = FfIcons.chatBubbles(),
                count = post.replyCount,
            )
            BottomIconButton(
                onClick = { postCardInteractions.onBoostClicked(post.statusId, !post.userBoosted) },
                painter = FfIcons.boost(),
                count = post.boostCount,
                highlighted = post.userBoosted,
            )
            BottomIconButton(
                onClick = { postCardInteractions.onFavoriteClicked(post.statusId, !post.isFavorited) },
                painter = if (post.isFavorited) FfIcons.heartFilled() else FfIcons.heart(),
                count = post.favoriteCount,
                highlighted = post.isFavorited,
                highlightColor = FfTheme.colors.textWarning,
            )
            BottomIconButton(
                onClick = {
                    post.url?.let { url ->
                        shareUrl(url, context)
                    }
                },
                painter = FfIcons.share(),
            )
        }
    ) {measurables, constraints ->
        val placeables =
            measurables.map {
                it.measure(
                    constraints.copy(
                        minWidth = 0,
                        minHeight = 0,
                    ),
                )
            }
        val replyPlaceable = placeables[0]
        val repostPlaceable = placeables[1]
        val likePlaceable = placeables[2]
        val sharePlaceable = placeables[3]
        val iconWidth = sharePlaceable.width
        layout(
            width = width.toPx().toInt(),
            height = replyPlaceable.height,
        ) {
            replyPlaceable.placeRelative(
                -iconWidth / 4,
                0,
            )
            repostPlaceable.placeRelative(
                (constraints.maxWidth / 3) - (iconWidth / 2),
                0
            )
            likePlaceable.placeRelative(
                (constraints.maxWidth / 3 * 2) - (iconWidth / 2),
                0
            )
            sharePlaceable.placeRelative(
                constraints.maxWidth - iconWidth / 4 * 3,
                0
            )
        }
    }
}

@Composable
private fun BottomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    count: String? = null,
    highlighted: Boolean = false,
    highlightColor: Color = FfTheme.colors.iconAccent,
) {
    val context = LocalContext.current
    Layout(
        modifier = modifier,
        content = {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    painter = painter,
                    contentDescription = "",
                    tint = if (highlighted) {
                        highlightColor
                    } else {
                        LocalContentColor.current
                    },
                )
            }
            Text(
                text = count ?: "",
                style = FfTheme.typography.labelXSmall,
                maxLines = 1,
            )
        }
    ) {measurables, constraints ->
        val placeables =
            measurables.map {
                it.measure(
                    constraints.copy(
                        minWidth = 0,
                        minHeight = 0,
                    ),
                )
            }
        val iconButtonPlaceable = placeables[0]
        val textPlaceable = placeables[1]
        layout(
            width = iconButtonPlaceable.width + textPlaceable.width,
            height = iconButtonPlaceable.height,
        ) {
            iconButtonPlaceable.placeRelative(
                x = 0,
                y = (constraints.maxHeight / 2) - (iconButtonPlaceable.height / 2),
            )
            textPlaceable.placeRelative(
                x = iconButtonPlaceable.width - 8f.toPxInt(context),
                y = (constraints.maxHeight / 2) - (textPlaceable.height / 2),
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(250.dp)
        ) {
            BottomRow(
                post = MainPostCardUiState(
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
                    overflowDropDownType = OverflowDropDownType.USER,
                ),
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowLargePreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(500.dp)
        ) {
            BottomRow(
                post = MainPostCardUiState(
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
                    overflowDropDownType = OverflowDropDownType.USER,
                ),
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowSmallPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(150.dp)
        ) {
            BottomRow(
                post = MainPostCardUiState(
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
                    overflowDropDownType = OverflowDropDownType.USER,
                ),
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}
