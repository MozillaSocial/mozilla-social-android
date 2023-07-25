package org.mozilla.social.core.ui.media

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import org.koin.androidx.compose.get
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.getFileType
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import java.io.File

//TODO fix loading video black box.
@Composable
fun MediaUpload(
    uri: Uri,
    loadState: LoadState,
    onRetryClicked: (Uri, File) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(FirefoxColor.Black)
    ) {
        val context = LocalContext.current

        val fileType = remember(uri) {
            uri.getFileType(context)
        }
        when (fileType) {
            FileType.VIDEO -> {
                VideoPlayer(uri = uri, loadState = loadState)
            }
            FileType.IMAGE -> Image(imageUri = uri, loadState = loadState)
            FileType.UNKNOWN -> {}
        }

        when (loadState) {
            LoadState.LOADING -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "Uploading...",
                        color = FirefoxColor.White,
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                    )
                }
            }
            LoadState.ERROR -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "Failed to upload image",
                        color = FirefoxColor.White,
                    )
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        onClick = {
                            onRetryClicked(uri, uri.toFile(context))
                        }
                    ) {
                        Text(
                            text = "Retry",
                            color = FirefoxColor.White,
                        )
                    }
                }
            }
            LoadState.LOADED -> {}
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun VideoPlayer(
    uri: Uri,
    loadState: LoadState,
) {
    val log: Log = get()
    val context = LocalContext.current

    val exoPlayer = remember(uri) {
        log.d("Exoplayer created")
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    if (loadState == LoadState.LOADED) {
        LaunchedEffect(Unit) {
            exoPlayer.play()
        }
    }

    DisposableEffect(
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                log.d("PlayerView created")
                PlayerView(it).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    controllerAutoShow = false
                    hideController()
                    player = exoPlayer
                }
            },
        )
    ) {
        onDispose {
            log.d("Exoplayer released")
            exoPlayer.release()
        }
    }
}

@Composable
private fun Image(
    imageUri: Uri,
    loadState: LoadState,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(
                if (loadState == LoadState.LOADING) {
                    0.3f
                } else {
                    1f
                }
            ),
        model = imageUri,
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}