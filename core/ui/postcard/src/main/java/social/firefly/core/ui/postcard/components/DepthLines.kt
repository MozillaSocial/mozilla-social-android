package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.toPx
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.postcard.DepthLinesUiState
import social.firefly.core.ui.postcard.PostCardUiState

/**
 * These are the lines that show in a thread in tree view
 */
@Composable
internal fun DepthLines(
    depthLinesUiState: DepthLinesUiState,
) {
    val spacingWidth = 8

    val postDepth = depthLinesUiState.postDepth
    val startingDepth = depthLinesUiState.startingDepth

    val width = maxOf(((postDepth - startingDepth + 1) * spacingWidth), 0)
    val lineColor = FfTheme.colors.borderPrimary
    val context = LocalContext.current

    if (width == 0) {
        Spacer(modifier = Modifier.width(spacingWidth.dp))
    }

    Canvas(
        modifier = Modifier
            .fillMaxHeight()
            .width(width.dp),
    ) {
        val height = size.height
        if (postDepth >= startingDepth) {

            // depth lines from other posts
            for (i in startingDepth until postDepth) {
                val drawDepth = i - startingDepth + 1
                val x = (spacingWidth * drawDepth).toFloat() + 1
                if (depthLinesUiState.depthLines.contains(i)) {
                    drawLine(
                        color = lineColor,
                        start = Offset(x.toPx(context), 0f.toPx(context)),
                        end = Offset(x.toPx(context), height),
                        strokeWidth = 2f.toPx(context),
                    )
                }
            }

            // curved line leading into the avatar
            if (postDepth > startingDepth) {
                val drawDepth = postDepth - startingDepth
                val x = (spacingWidth * drawDepth).toFloat() + 1

                val path = Path().apply {
                    moveTo(x.toPx(context), 0f.toPx(context))
                    lineTo(x.toPx(context), 18f.toPx(context))
                    quadraticBezierTo(
                        x1 = x.toPx(context),
                        y1 = 28f.toPx(context),
                        x2 = (x + 8).toPx(context),
                        y2 = 26f.toPx(context),
                    )
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = 2f.toPx(context),
                    )
                )
            }

            // new depth line coming out of avatar
            if (depthLinesUiState.depthLines.contains(depthLinesUiState.postDepth)) {
                val drawDepth = postDepth - startingDepth + 1
                val x = (spacingWidth * drawDepth).toFloat() + 1
                if (depthLinesUiState.showViewMoreRepliesText) {
                    // goes to the view more replies text
                    val endingHeight = height - 32f.toPx(context)
                    val path = Path().apply {
                        moveTo(
                            x = x.toPx(context),
                            y = 26f.toPx(context),
                        )
                        lineTo(
                            x = x.toPx(context),
                            y = endingHeight - 18f.toPx(context),
                        )
                        quadraticBezierTo(
                            x1 = x.toPx(context),
                            y1 = endingHeight,
                            x2 = (x + 8).toPx(context),
                            y2 = endingHeight,
                        )
                        lineTo(
                            x = (x + 40).toPx(context),
                            y = endingHeight,
                        )
                    }
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = 2f.toPx(context),
                        )
                    )
                } else {
                    // goes to the next post
                    drawLine(
                        color = lineColor,
                        start = Offset(x.toPx(context), 26f.toPx(context)),
                        end = Offset(x.toPx(context), height),
                        strokeWidth = 2f.toPx(context),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}