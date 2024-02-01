package org.mozilla.social.feature.media

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.common.utils.toPx
import org.mozilla.social.core.model.Attachment
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.common.MoSoSurface
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
internal fun MediaScreen(
    mediaBundle: NavigationDestination.Media.MediaBundle,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        val attachment = mediaBundle.attachments[mediaBundle.startIndex]

        Column {
            when (attachment) {
                is Attachment.Image -> ZoomableImage(attachment = attachment)
                else -> {}
            }
        }

    }
}

@Composable
private fun ZoomableImage(
    attachment: Attachment.Image,
) {
    BoxWithConstraints {
        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        val width = maxWidth.value
        val height = maxHeight.value

        val maxScale by remember(attachment, width, height) {
            derivedStateOf {
                val maxScaleHeight = ((attachment.meta?.original?.height ?: 0) / height).coerceAtLeast(1F)
                val maxScaleWidth = ((attachment.meta?.original?.width ?: 0) / width).coerceAtLeast(1F)
                max(maxScaleHeight, maxScaleWidth)
            }
        } 

        AsyncImage(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1F..maxScale)
                        val x = (pan.x * scale)
                        val y = (pan.y * scale)

                        val adjustedOffsetX: Float = width * (scale - 1).coerceAtLeast(0F)
                        val adjustedOffsetY = height * (scale - 1).coerceAtLeast(0F)

                        offsetX = (offsetX + x)
                            .coerceIn(-adjustedOffsetX..adjustedOffsetX)
                        offsetY = (offsetY + y)
                            .coerceIn(-adjustedOffsetY..adjustedOffsetY)
                    }
                }
                .fillMaxSize(),
            model = attachment.url,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
