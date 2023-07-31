package org.mozilla.social.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import org.mozilla.social.model.entity.Status

@Composable
fun PostCard(status: Status) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (status.boostedStatus != null) {
            TopRowMetaData(text = "${status.account.username} boosted", imageVector = Icons.Default.Repeat)
            MainPost(status = status.boostedStatus!!)
        } else {
            status.inReplyToAccountName?.let { replyAccountName ->
                TopRowMetaData(text = "In reply to $replyAccountName", imageVector = Icons.Default.Reply)
            }
            MainPost(status = status)
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
private fun MainPost(status: Status) {
    MetaData(status = status)
    val spannedText = HtmlCompat.fromHtml(status.content, 0)
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp)
            .wrapContentHeight(),
        factory = { MaterialTextView(it) },
        update = { it.text = spannedText }
    )
    status.mediaAttachments.forEach {
        AsyncImage(
            model = it.previewUrl,
            contentDescription = it.description
        )
        Spacer(modifier = Modifier.height(4.dp))
    }

    BottomRow(status)
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
private fun BottomRow(status: Status) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomIconButton(
            onClick = { /*TODO*/ },
            imageVector = Icons.Default.Reply,
            count = status.repliesCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { /*TODO*/ },
            imageVector = Icons.Default.Repeat,
            count = status.boostsCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { /*TODO*/ },
            imageVector = Icons.Default.StarBorder,
            count = status.favouritesCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { /*TODO*/ },
            imageVector = Icons.Default.Reply,
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