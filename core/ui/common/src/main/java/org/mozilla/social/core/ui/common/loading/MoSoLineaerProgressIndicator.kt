package org.mozilla.social.core.ui.common.loading

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
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoSoLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MoSoLinearProgressIndicatorDefaults.linearColor(),
    trackColor: Color = MoSoLinearProgressIndicatorDefaults.linearTrackColor(),
    strokeCap: StrokeCap = MoSoLinearProgressIndicatorDefaults.strokeCap,
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
fun MoSoLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MoSoLinearProgressIndicatorDefaults.linearColor(),
    trackColor: Color = MoSoLinearProgressIndicatorDefaults.linearTrackColor(),
    strokeCap: StrokeCap = MoSoLinearProgressIndicatorDefaults.strokeCap,
) {
    LinearProgressIndicator(
        modifier = modifier,
        progress = progress,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap
    )
}

object MoSoLinearProgressIndicatorDefaults {
    @Composable
    fun linearColor() = MoSoTheme.colors.actionPrimary

    @Composable
    fun linearTrackColor() = MoSoTheme.colors.layer1

    val strokeCap = StrokeCap.Round
}