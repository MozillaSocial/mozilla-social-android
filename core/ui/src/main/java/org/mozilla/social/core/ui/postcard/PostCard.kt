package org.mozilla.social.core.ui.postcard

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import org.mozilla.social.common.utils.DimenUtil
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.media.MediaDisplay
import org.mozilla.social.core.ui.poll.Poll

@Composable
fun PostCard(
    post: PostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        post.topRowMetaDataUiState?.let { TopRowMetaData(it) }
        MainPost(post.mainPostCardUiState, postCardInteractions)
    }
}

@Composable
private fun TopRowMetaData(
    topRowMetaDataUiState: TopRowMetaDataUiState,
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            imageVector = topRowMetaDataUiState.icon,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = topRowMetaDataUiState.text,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MainPost(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    MetaData(post = post)
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp),
        factory = {
            TextView(it).apply {
                // there is an extra chunk of padding added, so lets remove some of that
                setPadding(0, 0, 0, DimenUtil.dpToPxInt(context, -20f))
            }
        },
        update = { it.text = post.statusText }
    )
    MediaDisplay(attachments = post.mediaAttachments)
    post.pollUiState?.let {
        Poll(
            it,
            postCardInteractions,
        )
    }

    BottomRow(post, postCardInteractions)
}

@Composable
private fun MetaData(
    post: MainPostCardUiState,
) {
    Row {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.CenterVertically),
            model = post.profilePictureUrl,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Text(
                text = post.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = post.postMetaDataText,
                fontSize = 12.sp
            )
        }

    }

}

@Composable
private fun BottomRow(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomIconButton(
            onClick = { postCardInteractions.onReplyClicked(post.statusId) },
            imageVector = Icons.Default.Reply,
            count = post.replyCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onBoostClicked() },
            imageVector = Icons.Default.Repeat,
            count = post.boostCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onFavoriteClicked() },
            imageVector = Icons.Default.StarBorder,
            count = post.favoriteCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onShareClicked() },
            imageVector = Icons.Default.Share,
            count = 0,
        )
    }
}

@Composable
private fun BottomIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    count: Long,
) {
    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = onClick
    ) {
        Row {
            Icon(
                imageVector = imageVector,
                ""
            )
            if (count > 0) {
                Text(text = "$count")
            }
        }
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    MozillaSocialTheme {
//        PostCard(
//            status =
//                Status(
//                    "1",
//                    "asdf",
//                     Account("1", username = "username"),
//                    content = "here's a post",
//                    isSensitive = false,
//                )
//            )
    }
}