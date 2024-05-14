package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.toPx
import social.firefly.common.utils.toPxInt
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.unbookmarkAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.unfavoriteAccountConfirmationDialog
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.postCardUiStatePreview
import kotlin.math.roundToInt

@Suppress("MagicNumber", "LongMethod")
@Composable
internal fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    val unfavoriteStatusDialog = unfavoriteAccountConfirmationDialog {
        postCardInteractions.onFavoriteClicked(post.statusId, false)
    }

    val unbookmarkStatusDialog = unbookmarkAccountConfirmationDialog {
        postCardInteractions.onBookmarkClicked(post.statusId, false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = object : Arrangement.Horizontal {
                override fun Density.arrange(
                    totalSize: Int,
                    sizes: IntArray,
                    layoutDirection: LayoutDirection,
                    outPositions: IntArray
                ) {
                    if (sizes.isEmpty()) return

                    val iconSize = 50f.toPx(context)
                    val noOfGaps = maxOf(sizes.lastIndex, 1)
                    val gapSize = ((totalSize - iconSize) / noOfGaps) + 6f.toPxInt(context)

                    var currentPosition = (-6f).toPx(context)
                    sizes.forEachIndexed { index, _ ->
                        outPositions[index] = currentPosition.roundToInt()
                        currentPosition += gapSize
                    }
                }
            },
        ) {
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
        }
        BottomIconButton(
            modifier = Modifier.width(28.dp),
            onClick = {
                post.url?.let { url ->
                    shareUrl(url, context)
                }
            },
            painter = FfIcons.share(),
        )
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = object : Arrangement.Horizontal {
            override fun Density.arrange(
                totalSize: Int,
                sizes: IntArray,
                layoutDirection: LayoutDirection,
                outPositions: IntArray
            ) {
                if (sizes.isEmpty()) return

                outPositions[0] = 0
                if (outPositions.size >= 2) {
                    outPositions[1] = 32f.toPxInt(context)
                }
            }
        }
    ) {
        IconButton(
            modifier = Modifier.width(36.dp),
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
        count?.let {
            Text(
                text = it,
                style = FfTheme.typography.labelXSmall,
                maxLines = 1,
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
