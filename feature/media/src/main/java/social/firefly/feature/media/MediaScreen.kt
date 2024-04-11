package social.firefly.feature.media

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.Attachment
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.appbar.FfTopBarDefaults
import social.firefly.core.ui.common.media.VideoPlayer
import social.firefly.core.ui.common.media.calculateAspectRatio
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MediaScreen(
    attachments: List<Attachment>,
    selectedIndex: Int,
    mediaInteractions: MediaInteractions,
) {
    val attachment = attachments[selectedIndex]
    val context = LocalContext.current
    var altTextVisible by remember {
        mutableStateOf(false)
    }
    var uiVisible by remember {
        mutableStateOf(true)
    }
    val pagerState = rememberPagerState(
        initialPage = selectedIndex
    ) { attachments.size }

    val visibilityClick = {
        uiVisible = !uiVisible
        altTextVisible = false
    }

    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when (attachment) {
            is Attachment.Image -> ImagePager(
                attachments = attachments,
                pagerState = pagerState,
                onClick = visibilityClick
            )

            is Attachment.Video -> VideoContent(
                onClick = visibilityClick,
                attachment = attachment,
                controlsVisible = uiVisible,
            )
            is Attachment.Gifv -> VideoContent(
                onClick = visibilityClick,
                attachment = attachment,
                controlsVisible = uiVisible,
            )
            else -> {}
        }

        AnimatedVisibility(
            visible = uiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FfCloseableTopAppBar(
                modifier = Modifier
                    .zIndex(1f),
                colors = FfTopBarDefaults.colors(
                    containerColor = FfTheme.colors.layer1.copy(alpha = 0.5f)
                ),
                actions = {
                    attachments[pagerState.currentPage].description?.let {
                        IconButton(onClick = { altTextVisible = !altTextVisible }) {
                            MediumTextLabel(
                                text = stringResource(id = R.string.alt_text_label)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val uri = Uri.parse(attachments[pagerState.currentPage].url)
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
                            modifier = Modifier.size(FfIcons.Sizes.normal),
                            painter = FfIcons.downloadSimple(),
                            contentDescription = stringResource(id = R.string.download_content_description),
                        )
                    }
                }
            )
        }
    }


    attachments[pagerState.currentPage].description?.let { description ->
        AltText(
            description = description,
            visible = altTextVisible,
        )
    }
}

@Composable
private fun AltText(
    description: String,
    visible: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                        )
                    )
                    .background(FfTheme.colors.layer2)
                    .padding(16.dp)
            ) {
                MediumTextBody(text = description)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    attachments: List<Attachment>,
    pagerState: PagerState,
    onClick: () -> Unit,
) {
    val scale = remember { mutableFloatStateOf(1f) }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = scale.floatValue == 1f,
    ) {
        when (val attachment = attachments[it]) {
            is Attachment.Image -> ZoomableImage(
                attachment = attachment,
                pagerState,
                scale,
                onClick = onClick,
            )

            else -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ZoomableImage(
    attachment: Attachment.Image,
    pagerState: PagerState,
    scale: MutableFloatState = remember { mutableFloatStateOf(1f) },
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var width by remember { mutableFloatStateOf(1f) }
        var height by remember { mutableFloatStateOf(1f) }
        var innerScale by scale
        var translationX by remember { mutableFloatStateOf(0f) }
        var translationY by remember { mutableFloatStateOf(0f) }

        val maxScale by remember(attachment, width, height) {
            derivedStateOf {
                val maxScaleHeight =
                    ((attachment.meta?.original?.height ?: 0) / height).coerceAtLeast(1f)
                val maxScaleWidth =
                    ((attachment.meta?.original?.width ?: 0) / width).coerceAtLeast(1f)
                max(maxScaleHeight, maxScaleWidth) * 3
            }
        }

        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .onSizeChanged {
                    width = it.width.toFloat()
                    height = it.height.toFloat()
                }
                .graphicsLayer(
                    scaleX = innerScale,
                    scaleY = innerScale,
                    translationX = translationX,
                    translationY = translationY
                )
                .pointerInput(Unit) {
                    detectTransformGesturesInsidePager(
                        currentScale = scale,
                        pagerState = pagerState,
                    ) { centroid, pan, zoom, _ ->
                        innerScale = (innerScale * zoom).coerceIn(1f..maxScale)
                        val panX = (pan.x * innerScale)
                        val panY = (pan.y * innerScale)

                        val translationLimitX =
                            (width / 2) * (innerScale - 1).coerceAtLeast(0F)
                        val translationLimitY =
                            (height / 2) * (innerScale - 1).coerceAtLeast(0F)

                        val centroidDistX = (translationX + (centroid.x - (width / 2)) * innerScale)
                        val centroidTranslationX = centroidDistX * (zoom - 1) * 2

                        val centroidDistY =
                            (translationY + (centroid.y - (height / 2)) * innerScale)
                        val centroidTranslationY =
                            centroidDistY * (zoom - 1) * 2

                        translationX = (translationX + panX - centroidTranslationX)
                            .coerceIn(-translationLimitX..translationLimitX)
                        translationY = (translationY + panY - centroidTranslationY)
                            .coerceIn(-translationLimitY..translationLimitY)
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { offset ->
                            innerScale = if (innerScale >= 2f) {
                                1f
                            } else {
                                (innerScale * 2).coerceAtMost(maxScale)
                            }

                            val translationLimitX =
                                (width / 2) * (innerScale - 1).coerceAtLeast(0F)
                            val translationLimitY =
                                (height / 2) * (innerScale - 1).coerceAtLeast(0F)

                            translationX = (translationX - (offset.x - (width / 2)) * innerScale)
                                .coerceIn(-translationLimitX..translationLimitX)
                            translationY = (translationY - (offset.y - (height / 2)) * innerScale)
                                .coerceIn(-translationLimitY..translationLimitY)
                        },
                        onTap = {
                            onClick()
                        }
                    )
                },
            model = attachment.url,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun VideoContent(
    attachment: Attachment.Video,
    onClick: () -> Unit,
    controlsVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        VideoPlayer(
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(attachment.meta?.calculateAspectRatio() ?: 1f),
            uri = Uri.parse(attachment.url),
            onClick = onClick,
            controlsVisible = controlsVisible,
        )
    }
}

@Composable
private fun VideoContent(
    attachment: Attachment.Gifv,
    onClick: () -> Unit,
    controlsVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        VideoPlayer(
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(attachment.meta?.calculateAspectRatio() ?: 1f),
            uri = Uri.parse(attachment.url),
            onClick = onClick,
            controlsVisible = controlsVisible,
        )
    }
}

@Preview
@Composable
private fun MediaScreenPreview() {
    PreviewTheme(
        modules = listOf(
            navigationModule
        )
    ) {
        MediaScreen(
            attachments = listOf(
                Attachment.Image(
                    attachmentId = "",
                )
            ),
            selectedIndex = 0,
            mediaInteractions = MediaInteractionsNoOp
        )
    }
}
