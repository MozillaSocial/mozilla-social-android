package org.mozilla.social.feature.media

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.toPx
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.model.Attachment
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import kotlin.math.max

@Composable
internal fun MediaScreen(
    mediaBundle: NavigationDestination.Media.MediaBundle,
    viewModel: MediaViewModel = koinViewModel(parameters = { parametersOf(mediaBundle.startIndex) })
) {
    val index by viewModel.index.collectAsStateWithLifecycle()

    MediaScreen(
        attachments = mediaBundle.attachments,
        selectedIndex = index,
        mediaInteractions = viewModel,
    )
}

@Composable
private fun MediaScreen(
    attachments: List<Attachment>,
    selectedIndex: Int,
    mediaInteractions: MediaInteractions,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        val attachment = attachments[selectedIndex]
        val context = LocalContext.current

        Column {
            MoSoCloseableTopAppBar(
                modifier = Modifier
                    .zIndex(1f),
                actions = {
                    IconButton(
                        onClick = {
                            val uri = Uri.parse(attachment.url)
                            val fileName = uri.lastPathSegment

                            DownloadManager.Request(uri).apply {
                                setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_DOWNLOADS,
                                    fileName,
                                )
                                (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
                                    .enqueue(this)
                            }

                            mediaInteractions.onDownloadClicked(fileName ?: "")
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(MoSoIcons.Sizes.normal),
                            painter = MoSoIcons.downloadSimple(),
                            contentDescription = stringResource(id = R.string.download_content_description),
                        )
                    }
                }
            )
            when (attachment) {
                is Attachment.Image -> ZoomableImage(
                    attachment = attachment,
                )
                else -> {}
            }
        }

    }
}

@Composable
private fun ZoomableImage(
    attachment: Attachment.Image,
) {
    val density = LocalDensity.current
    var width by remember { mutableFloatStateOf(0f) }
    var height by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                with (density) {
                    width = it.width.toDp().value
                    height = it.height.toDp().value
                }
            }
    ) {
        if (width == 0f) return@Box
        var scale by remember { mutableFloatStateOf(1f) }
        var translationX by remember { mutableFloatStateOf(0f) }
        var translationY by remember { mutableFloatStateOf(0f) }

        val maxScale by remember(attachment, width, height) {
            derivedStateOf {
                val maxScaleHeight = ((attachment.meta?.original?.height ?: 0) / height).coerceAtLeast(1f)
                val maxScaleWidth = ((attachment.meta?.original?.width ?: 0) / width).coerceAtLeast(1f)
                max(maxScaleHeight, maxScaleWidth)
            }
        }

        AsyncImage(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = translationX,
                    translationY = translationY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        with (density) {
                            scale = (scale * zoom).coerceIn(1f..maxScale)
                            val x = (pan.x * scale)
                            val y = (pan.y * scale)

                            val offsetLimitX: Float = width * (scale - 1).coerceAtLeast(0F)
                            val offsetLimitY = height * (scale - 1).coerceAtLeast(0F)

                            translationX = (translationX + x)
                                .coerceIn(-offsetLimitX..offsetLimitX)
                            translationY = (translationY + y)
                                .coerceIn(-offsetLimitY..offsetLimitY)
                        }
                    }
                }
                .fillMaxSize(),
            model = attachment.url,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
