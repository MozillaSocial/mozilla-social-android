package org.mozilla.social.core.ui.media

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import org.mozilla.social.core.ui.Radius
import org.mozilla.social.model.Attachment

@Suppress("MagicNumber")
@Composable
fun MediaDisplay(
    attachments: List<Attachment>
) {
    when (attachments.size) {
        1 -> {
            SingleAttachment(attachment = attachments.first())
        }
        2 -> {
            AttachmentRow(
                attachment1 = attachments.first(),
                attachment2 = attachments[1]
            )
        }
        3 -> {
            SingleAttachment(attachment = attachments.first())
            AttachmentRow(
                attachment1 = attachments[1],
                attachment2 = attachments[2]
            )
        }
        4 -> {
            AttachmentRow(
                attachment1 = attachments.first(),
                attachment2 = attachments[1]
            )
            AttachmentRow(
                attachment1 = attachments[2],
                attachment2 = attachments[3]
            )
        }
    }
}

@Composable
private fun SingleAttachment(
    attachment: Attachment,
) {
    when (attachment) {
        is Attachment.Image -> {
            Attachment(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(attachment.meta.original?.aspectRatio ?: 1f),
                attachment = attachment
            )
        }
        is Attachment.Gifv -> {
            val aspectRatio by remember {
                mutableFloatStateOf(attachment.meta.calculateAspectRatio())
            }
            attachment.url?.toUri()?.let {
                VideoPlayer(
                    uri = it,
                    aspectRatio = aspectRatio,
                )
            }
        }
        is Attachment.Video -> {
            val aspectRatio by remember {
                mutableFloatStateOf(attachment.meta.aspectRatio ?: 1f)
            }
            attachment.url?.toUri()?.let {
                VideoPlayer(
                    uri = it,
                    aspectRatio = aspectRatio
                )
            }
        }
        else -> {}
    }
}

/**
 * For some reason the server might not return an aspect ratio
 */
private fun Attachment.Gifv.Meta.calculateAspectRatio(): Float =
    when {
        aspectRatio != null -> aspectRatio!!
        original?.width != null && original?.height != null ->
            (original!!.width!!.toFloat() / original!!.height!!.toFloat())
        else -> 1f
    }

@Composable
private fun Attachment(
    modifier: Modifier = Modifier,
    attachment: Attachment
) {
    AsyncImage(
        modifier = modifier
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
    Row {
        Attachment(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            attachment = attachment1
        )
        Attachment(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            attachment = attachment2
        )
    }
}