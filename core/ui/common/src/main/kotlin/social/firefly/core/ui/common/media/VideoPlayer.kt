package social.firefly.core.ui.common.media

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import social.firefly.common.LoadState
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.NoTouchOverlay
import social.firefly.core.ui.common.utils.media
import timber.log.Timber

private const val TAG = "VideoPlayer"

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun VideoPlayer(
    uri: Uri,
    loadState: LoadState = LoadState.LOADED,
    aspectRatio: Float = 1f,
    onVideoClicked: (() -> Unit)? = null,
) {
    Box(
        modifier =
        Modifier
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(FfRadius.media))
            .clickable(
                enabled = onVideoClicked != null,
            ) {
                onVideoClicked?.let { it() }
            },
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

        if (loadState == LoadState.LOADED) {
            LaunchedEffect(Unit) {
                exoPlayer.play()
            }
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
                    isClickable = false
                }
            },
        )

        DisposableEffect(Unit) {
            onDispose {
                Timber.tag(TAG).d("Exoplayer released")
                exoPlayer.release()
            }
        }
        if (onVideoClicked == null) {
            NoTouchOverlay()
        }

        // Mute button
        if (loadState == LoadState.LOADED) {
            MuteButton(exoPlayer = exoPlayer)
        }
    }
}

@Composable
private fun BoxScope.MuteButton(exoPlayer: ExoPlayer) {
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
        },
    ) {
        if (muted.value) {
            Icon(
                painter = FfIcons.volumeMute(),
                contentDescription = null,
                tint = FfTheme.colors.playerControlsForeground,
            )
        } else {
            Icon(
                painter = FfIcons.volumeUp(),
                contentDescription = null,
                tint = FfTheme.colors.playerControlsForeground,
            )
        }
    }
}
