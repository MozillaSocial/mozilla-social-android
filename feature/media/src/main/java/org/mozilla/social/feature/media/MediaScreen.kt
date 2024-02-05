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
import androidx.compose.ui.Alignment
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
    var width by remember { mutableFloatStateOf(0f) }
    var height by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                width = it.width.toFloat()
                height = it.height.toFloat()
            }
    ) {
        if (width == 0f) return@Box
        var scale by remember { mutableFloatStateOf(1f) }
        var translationX by remember { mutableFloatStateOf(0f) }
        var translationY by remember { mutableFloatStateOf(0f) }

        // 2x 100% zoom
        val maxScale by remember(attachment, width, height) {
            derivedStateOf {
                val maxScaleHeight = ((attachment.meta?.original?.height ?: 0) / height).coerceAtLeast(1f)
                val maxScaleWidth = ((attachment.meta?.original?.width ?: 0) / width).coerceAtLeast(1f)
                max(maxScaleHeight, maxScaleWidth) * 2
            }
        }

        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = translationX,
                    translationY = translationY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        println("johnny ================================")
                        println("johnny centroid $centroid")
                        println("johnny zoom $zoom")
                        println("johnny translationX $translationX")
                        println("johnny translationY $translationY")
                        scale = (scale * zoom).coerceIn(1f..maxScale)
                        val x = (pan.x * scale)
                        val y = (pan.y * scale)

                        val offsetLimitX: Float = (width / 2) * (scale - 1).coerceAtLeast(0F)
                        val offsetLimitY = (height / 2) * (scale - 1).coerceAtLeast(0F)

                        val centroidTranslationX = (translationX + centroid.x - (width / 2)) * (scale - 1)
                        println("johnny centroidTranslationX $centroidTranslationX")
                        val centroidTranslationY = (translationY + centroid.y - (height / 2)) * (scale - 1)
                        println("johnny centroidTranslationY $centroidTranslationY")

                        translationX = (translationX + x - centroidTranslationX)
                            .coerceIn(-offsetLimitX..offsetLimitX)
                        translationY = (translationY + y - centroidTranslationY)
                            .coerceIn(-offsetLimitY..offsetLimitY)
                    }
                },
            model = attachment.url,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
