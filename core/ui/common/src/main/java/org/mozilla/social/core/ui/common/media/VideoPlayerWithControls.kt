package org.mozilla.social.core.ui.common.media

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.ui.common.NoTouchOverlay
import org.mozilla.social.core.ui.common.R
import org.mozilla.social.core.ui.common.loading.MoSoLinearProgressIndicator
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import timber.log.Timber
import kotlin.math.roundToLong

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
                    volume = 1f
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

        VideoControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            exoPlayer = exoPlayer,
        )
    }
}

@OptIn(UnstableApi::class)
@Suppress("MagicNumber")
@Composable
private fun VideoControls(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val muted = remember { mutableStateOf(exoPlayer.volume == 0f) }
    var isPlaying by remember { mutableStateOf(true) }

    LaunchedEffect(exoPlayer) {
        while (true) {
            progress = exoPlayer.contentPosition / exoPlayer.duration.toFloat()
            delay(100)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(FirefoxColor.DarkGrey90A80),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            isPlaying = exoPlayer.isPlaying
        }) {
            Icon(
                modifier = Modifier
                    .size(MoSoIcons.Sizes.small),
                painter = if (isPlaying) {
                    MoSoIcons.pause()
                } else {
                    MoSoIcons.play()
                },
                contentDescription = if (isPlaying) {
                    stringResource(id = R.string.pause)
                } else {
                    stringResource(id = R.string.play)
                },
                tint = FirefoxColor.White,
            )
        }

        MoSoLinearProgressIndicator(
            modifier = Modifier.weight(1f),
            progress = progress,
            onTouch = {
                exoPlayer.seekTo((exoPlayer.duration * it).roundToLong())
            },
        )

        Spacer(modifier = Modifier.width(8.dp))

        SmallTextLabel(
            text = "${exoPlayer.contentPosition.toTimeString()}/${exoPlayer.duration.toTimeString()}",
            color = FirefoxColor.White,
        )

        IconButton(
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
            Icon(
                modifier = Modifier
                    .size(MoSoIcons.Sizes.small),
                painter = if (muted.value) {
                    MoSoIcons.volumeMute()
                } else {
                    MoSoIcons.volumeUp()
                },
                contentDescription = stringResource(id = R.string.mute),
                tint = FirefoxColor.White,
            )
        }
    }
}

private fun Long.toTimeString(): String {
    if (this < 0) return "0:00"
    val seconds = this / 1_000
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = seconds.mod(60)
    val remainingSecondsString: String = if (remainingSeconds < 10) {
        "0$remainingSeconds"
    } else {
        remainingSeconds.toString()
    }
    return "$minutes:$remainingSecondsString"
}
