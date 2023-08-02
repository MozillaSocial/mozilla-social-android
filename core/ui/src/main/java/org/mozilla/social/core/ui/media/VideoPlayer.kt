package org.mozilla.social.core.ui.media

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import org.koin.androidx.compose.get
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.ui.NoTouchOverlay

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun VideoPlayer(
    uri: Uri,
    loadState: LoadState = LoadState.LOADED,
) {
    Box {
        val log: Log = get()
        val context = LocalContext.current

        val exoPlayer = remember(uri) {
            log.d("Exoplayer created")
            ExoPlayer.Builder(context).build().apply {
                repeatMode = Player.REPEAT_MODE_ALL
                volume = 0f
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
        NoTouchOverlay()

        // Mute button
        if (loadState == LoadState.LOADED) {
            val muted = remember { mutableStateOf(exoPlayer.volume == 0f) }
            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = {
                    if (muted.value) {
                        muted.value = false
                        exoPlayer.volume = 1f
                    } else {
                        muted.value = true
                        exoPlayer.volume = 0f
                    }
                }
            ) {
                if (muted.value) {
                    Icon(
                        imageVector = Icons.Default.VolumeMute,
                        contentDescription = null,
                        tint = FirefoxColor.White,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = FirefoxColor.White,
                    )
                }
            }
        }
    }
}