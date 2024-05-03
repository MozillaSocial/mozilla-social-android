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
import social.firefly.core.ui.common.dialog.deleteStatusConfirmationDialog
import social.firefly.core.ui.common.dialog.unbookmarkAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.unfavoriteAccountConfirmationDialog
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.getMaxWidth
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.OverflowDropDownType
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostContentUiState
import social.firefly.core.ui.postcard.postCardUiStatePreview

@Suppress("MagicNumber", "LongMethod")
@Composable
internal fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    val width = getMaxWidth()

    val unfavoriteStatusDialog = unfavoriteAccountConfirmationDialog {
        postCardInteractions.onFavoriteClicked(post.statusId, false)
    }

    val unbookmarkStatusDialog = unbookmarkAccountConfirmationDialog {
        postCardInteractions.onBookmarkClicked(post.statusId, false)
    }

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
                onClick = {
                    if (post.shouldShowUnfavoriteConfirmation && post.isFavorited) {
                        unfavoriteStatusDialog.open()
                    } else {
                        postCardInteractions.onFavoriteClicked(post.statusId, !post.isFavorited)
                    }
                },
                painter = if (post.isFavorited) FfIcons.heartFilled() else FfIcons.heart(),
                count = post.favoriteCount,
                highlighted = post.isFavorited,
                highlightColor = FfTheme.colors.iconFavorite,
            )
            BottomIconButton(
                onClick = {
                    if (post.shouldShowUnbookmarkConfirmation && post.isBookmarked) {
                        unbookmarkStatusDialog.open()
                    } else {
                        postCardInteractions.onBookmarkClicked(post.statusId, !post.isBookmarked)
                    }
                },
                painter = if (post.isBookmarked) FfIcons.bookmarkFill() else FfIcons.bookmark(),
                highlighted = post.isBookmarked,
                highlightColor = FfTheme.colors.iconBookmark,
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
        val bookmarkPlaceable = placeables[3]
        val sharePlaceable = placeables[4]
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
                (constraints.maxWidth / 4) - (iconWidth / 2),
                0
            )
            likePlaceable.placeRelative(
                (constraints.maxWidth / 4 * 2) - (iconWidth / 2),
                0
            )
            bookmarkPlaceable.placeRelative(
                (constraints.maxWidth / 4 * 3) - (iconWidth / 2),
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
                post = postCardUiStatePreview,
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
                post = postCardUiStatePreview,
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
                post = postCardUiStatePreview,
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}
