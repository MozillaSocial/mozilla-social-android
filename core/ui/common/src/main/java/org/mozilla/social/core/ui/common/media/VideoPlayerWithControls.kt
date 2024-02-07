package org.mozilla.social.core.ui.common.media

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import org.mozilla.social.core.ui.common.NoTouchOverlay
import timber.log.Timber

private const val TAG = "VideoPlayer"

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    uri: Uri,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        val context = LocalContext.current

        val exoPlayer =
            remember(uri) {
                Timber.tag(TAG).d("Exoplayer created")
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ALL
                    volume = 0f
                    setMediaItem(MediaItem.fromUri(uri))
                    prepare()
                }
            }

        LaunchedEffect(Unit) {
            exoPlayer.play()
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                Timber.tag(TAG).d("PlayerView created")
                PlayerView(it).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    controllerAutoShow = false
                    hideController()
                    player = exoPlayer
                }
            },
        )

        DisposableEffect(Unit) {
            onDispose {
                Timber.tag(TAG).d("Exoplayer released")
                exoPlayer.release()
            }
        }
        NoTouchOverlay()

        // Mute button

    }
}