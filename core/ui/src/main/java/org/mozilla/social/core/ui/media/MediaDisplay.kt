package org.mozilla.social.core.ui.media

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.getFileType
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.ui.Radius
import org.mozilla.social.model.Attachment

@Suppress("MagicNumber")
@Composable
fun MediaDisplay(
    attachments: List<Attachment>
) {
    // there can be a maximum of 4 attachments.
    // videos must be the only attachment
    when (attachments.size) {
        1 -> {
            val attachment = attachments.first()
            val uri = attachment.url?.toUri() ?: return
            when (attachment) {
                is Attachment.Image,
                is Attachment.Gifv -> Attachment(
                    modifier = Modifier.fillMaxWidth(),
                    attachment = attachment
                )
                is Attachment.Video -> VideoPlayer(uri = uri)
                else -> {}
            }
        }
        2 -> {
            AttachmentRow(attachment1 = attachments.first(), attachment2 = attachments[1])
        }
        3 -> {
            Attachment(
                modifier = Modifier.fillMaxWidth(),
                attachment = attachments.first()
            )
            AttachmentRow(attachment1 = attachments[1], attachment2 = attachments[2])
        }
        4 -> {
            AttachmentRow(attachment1 = attachments.first(), attachment2 = attachments[1])
            AttachmentRow(attachment1 = attachments[2], attachment2 = attachments[3])
        }
    }
}

@Composable
private fun Attachment(
    modifier: Modifier = Modifier,
    attachment: Attachment
) {
    val aspectRatio = when (attachment) {
        is Attachment.Gifv -> attachment.meta.aspect?.toFloat()
        is Attachment.Image -> attachment.meta.aspect?.toFloat()
        else -> null
    }
    AsyncImage(
        modifier = modifier
            .aspectRatio(aspectRatio ?: 2f)
            .padding(2.dp)
            .clip(RoundedCornerShape(Radius.media)),
        model = attachment.previewUrl,
        contentDescription = attachment.description,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun AttachmentRow(
    attachment1: Attachment,
    attachment2: Attachment,
) {
    val imageHeight = LocalConfiguration.current.screenWidthDp / 2
    Row {
        Attachment(
            modifier = Modifier.weight(1f).height(imageHeight.dp),
            attachment = attachment1
        )
        Attachment(
            modifier = Modifier.weight(1f).height(imageHeight.dp),
            attachment = attachment2
        )
    }
}