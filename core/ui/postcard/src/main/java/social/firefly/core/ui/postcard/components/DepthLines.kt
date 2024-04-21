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
import social.firefly.core.ui.postcard.ExpandRepliesButtonUiState

/**
 * These are the lines that show in a thread in tree view
 */
@Suppress("LongMethod")
@Composable
internal fun DepthLines(
    depthLinesUiState: DepthLinesUiState,
) {
    val spacingWidth = 12

    val postDepth = depthLinesUiState.postDepth
    val startingDepth = 1

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

            val avatarCenterY = 26f
            val avatarCenterYPx = avatarCenterY.toPx(context)
            val plusIconCenterY = height - 26f.toPx(context)
            val strokeWidth = 2f.toPx(context)

            // depth lines from other posts
            for (drawDepth in startingDepth until postDepth) {
                val x = (spacingWidth * drawDepth).toFloat() + 1
                if (depthLinesUiState.depthLines.contains(drawDepth)) {
                    drawLine(
                        color = lineColor,
                        start = Offset(x.toPx(context), 0f.toPx(context)),
                        end = Offset(x.toPx(context), height),
                        strokeWidth = strokeWidth,
                    )
                }
            }

            // curved line leading into the avatar
            if (postDepth > startingDepth) {
                val drawDepth = postDepth - startingDepth
                val x = (spacingWidth * drawDepth).toFloat() + 1
                val curveStartY = avatarCenterY - spacingWidth

                val path = Path().apply {
                    moveTo(x.toPx(context), 0f.toPx(context))
                    lineTo(x.toPx(context), curveStartY.toPx(context))
                    quadraticBezierTo(
                        x1 = x.toPx(context),
                        y1 = avatarCenterYPx,
                        x2 = (x + spacingWidth).toPx(context),
                        y2 = avatarCenterYPx,
                    )
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = strokeWidth,
                    )
                )
            }

            // new depth line coming out of avatar
            if (depthLinesUiState.depthLines.contains(depthLinesUiState.postDepth)) {
                val drawDepth = postDepth - startingDepth + 1
                val x = (spacingWidth * drawDepth).toFloat() + 1
                val xPx = x.toPx(context)
                if (depthLinesUiState.showViewMoreRepliesText) {
                    // goes to the view more replies text
                    val viewMoreRepliesTextCenterY = height - 32f.toPx(context)
                    val curveStartY = viewMoreRepliesTextCenterY - spacingWidth.toFloat().toPx(context)

                    val path = Path().apply {
                        moveTo(
                            x = xPx,
                            y = avatarCenterYPx,
                        )
                        lineTo(
                            x = xPx,
                            y = curveStartY,
                        )
                        quadraticBezierTo(
                            x1 = xPx,
                            y1 = viewMoreRepliesTextCenterY,
                            x2 = (x + spacingWidth).toPx(context),
                            y2 = viewMoreRepliesTextCenterY,
                        )
                        lineTo(
                            x = (x + 40).toPx(context),
                            y = viewMoreRepliesTextCenterY,
                        )
                    }
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = strokeWidth,
                        )
                    )
                } else if (depthLinesUiState.expandRepliesButtonUiState == ExpandRepliesButtonUiState.PLUS) {
                    // goes to the plus icon
                    drawLine(
                        color = lineColor,
                        start = Offset(xPx, avatarCenterYPx),
                        end = Offset(xPx, plusIconCenterY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                } else {
                    // goes to the next post
                    drawLine(
                        color = lineColor,
                        start = Offset(xPx, avatarCenterYPx),
                        end = Offset(xPx, height),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}