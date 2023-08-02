package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.android.material.textview.MaterialTextView
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.media.MediaDisplay
import org.mozilla.social.core.ui.poll.Poll
import org.mozilla.social.model.Status

@Composable
fun PostCard(
    status: Status,
    postCardInteractions: PostCardInteractions,
) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (status.boostedStatus != null) {
            TopRowMetaData(text = "${status.account.username} boosted", imageVector = Icons.Default.Repeat)
            MainPost(status.boostedStatus!!, postCardInteractions)
        } else {
            status.inReplyToAccountName?.let { replyAccountName ->
                TopRowMetaData(text = "In reply to $replyAccountName", imageVector = Icons.Default.Reply)
            }
            MainPost(status, postCardInteractions)
        }
    }
}

@Composable
private fun TopRowMetaData(
    text: String,
    imageVector: ImageVector,
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            imageVector = imageVector,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MainPost(
    status: Status,
    postCardInteractions: PostCardInteractions,
) {
    MetaData(status = status)
    val spannedText = remember(status.content) {
        HtmlCompat.fromHtml(status.content, 0)
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp)
            .wrapContentHeight(),
        factory = { MaterialTextView(it) },
        update = { it.text = spannedText }
    )
    MediaDisplay(attachments = status.mediaAttachments)
    status.poll?.let {
        Poll(
            isUserCreatedPoll = false, //TODO check if status.account is my account
            it,
            postCardInteractions,
        )
    }

    BottomRow(status, postCardInteractions)
}

@Composable
private fun MetaData(status: Status) {
    Row {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.CenterVertically),
            model = status.account.avatarStaticUrl,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Text(
                text = status.account.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${status.createdAt.timeSinceNow()} - @${status.account.acct}",
                fontSize = 12.sp
            )
        }

    }

}

@Composable
private fun BottomRow(
    status: Status,
    postCardInteractions: PostCardInteractions,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomIconButton(
            onClick = { postCardInteractions.onReplyClicked(status.statusId) },
            imageVector = Icons.Default.Reply,
            count = status.repliesCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onBoostClicked() },
            imageVector = Icons.Default.Repeat,
            count = status.boostsCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onFavoriteClicked() },
            imageVector = Icons.Default.StarBorder,
            count = status.favouritesCount,
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