package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.utils.getMaxWidth
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.PostCardInteractions

@Composable
internal fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    Row(
        modifier =
        modifier
            .requiredWidth(getMaxWidth() + 20.dp),
    ) {
        BottomIconButton(
            modifier = Modifier.weight(1f),
            onClick = { postCardInteractions.onReplyClicked(post.statusId) },
            painter = FfIcons.chatBubbles(),
            count = post.replyCount,
        )
        BottomIconButton(
            modifier = Modifier.weight(1f),
            onClick = { postCardInteractions.onBoostClicked(post.statusId, !post.userBoosted) },
            painter = FfIcons.boost(),
            count = post.boostCount,
            highlighted = post.userBoosted,
        )
        BottomIconButton(
            modifier = Modifier.weight(1f),
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
    Row(
        modifier = modifier,
    ) {
        IconButton(
            modifier =
            Modifier
                .width(IntrinsicSize.Max)
                .align(Alignment.CenterVertically),
            onClick = onClick,
        ) {
            Icon(
                painter = painter,
                contentDescription = "",
                tint =
                if (highlighted) {
                    highlightColor
                } else {
                    LocalContentColor.current
                },
            )
        }
        if (count != null) {
            Text(
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .offset(x = -6.dp),
                text = count,
            )
        }
    }
}