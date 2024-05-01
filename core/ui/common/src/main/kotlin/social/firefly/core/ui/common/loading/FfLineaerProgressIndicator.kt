package social.firefly.core.ui.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FfLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = FfLinearProgressIndicatorDefaults.linearColor(),
    trackColor: Color = FfLinearProgressIndicatorDefaults.linearTrackColor(),
    strokeCap: StrokeCap = FfLinearProgressIndicatorDefaults.strokeCap,
    onTouch: (value: Float) -> Unit,
) {
    var width by remember { mutableFloatStateOf(1f) }
    Box(
        modifier = modifier
            .pointerInteropFilter {
                onTouch(it.x / width)
                true
            }
            .onSizeChanged {
                width = it.width.toFloat()
            }
            .defaultMinSize(
                minHeight = 30.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = progress,
            color = color,
            trackColor = trackColor,
            strokeCap = strokeCap
        )
    }
}

@Composable
fun FfLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = FfLinearProgressIndicatorDefaults.linearColor(),
    trackColor: Color = FfLinearProgressIndicatorDefaults.linearTrackColor(),
    strokeCap: StrokeCap = FfLinearProgressIndicatorDefaults.strokeCap,
) {
    LinearProgressIndicator(
        modifier = modifier,
        progress = progress,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap
    )
}

object FfLinearProgressIndicatorDefaults {
    @Composable
    fun linearColor() = FfTheme.colors.actionPrimary

    @Composable
    fun linearTrackColor() = FfTheme.colors.layer1

    val strokeCap = StrokeCap.Round
}