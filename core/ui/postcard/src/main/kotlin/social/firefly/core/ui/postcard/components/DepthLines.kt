package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.toPx
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.postcard.DepthLinesUiState
import social.firefly.core.ui.postcard.ExpandRepliesButtonUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState

/**
 * These are the lines that show in a thread in tree view
 */
@Suppress("LongMethod")
@Composable
fun DepthLines(
    depthLinesUiState: DepthLinesUiState?,
) {
    Box {

        if (depthLinesUiState == null) {
            Spacer(modifier = Modifier.width(8.dp))
            return
        }

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

                val avatarCenterYPx = 26f.toPx(context)
                val plusIconCenterYPx = height - 26f.toPx(context)
                val strokeWidthPx = 2f.toPx(context)

                // depth lines from other posts
                for (drawDepth in startingDepth until postDepth) {
                    val x = (spacingWidth * drawDepth).toFloat() + 1
                    val xPx = x.toPx(context)
                    if (depthLinesUiState.depthLines.contains(drawDepth)) {
                        drawLine(
                            color = lineColor,
                            start = Offset(
                                x = xPx,
                                y = 0f,
                            ),
                            end = Offset(
                                x = xPx,
                                y = height,
                            ),
                            strokeWidth = strokeWidthPx,
                        )
                    }
                }

                if (depthLinesUiState.showViewMoreRepliesWithPlusButton) {
                    val drawDepth = postDepth - startingDepth
                    val x = (spacingWidth * drawDepth).toFloat() + 1
                    val yEnd = height / 2
                    val curveStartY = yEnd - spacingWidth.toFloat().toPx(context)

                    val path = Path().apply {
                        moveTo(
                            x = x.toPx(context),
                            y = 0f.toPx(context)
                        )
                        lineTo(
                            x = x.toPx(context),
                            y = curveStartY
                        )
                        quadraticBezierTo(
                            x1 = x.toPx(context),
                            y1 = yEnd,
                            x2 = (x + spacingWidth).toPx(context),
                            y2 = yEnd,
                        )
                        lineTo(
                            x = (x + spacingWidth + 40).toPx(context),
                            y = yEnd
                        )
                    }
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = strokeWidthPx,
                        )
                    )
                } else {
                    // curved line leading into the avatar
                    if (postDepth > startingDepth) {
                        val drawDepth = postDepth - startingDepth
                        val x = (spacingWidth * drawDepth).toFloat() + 1
                        val curveStartY = avatarCenterYPx - spacingWidth.toFloat().toPx(context)

                        val path = Path().apply {
                            moveTo(
                                x = x.toPx(context),
                                y = 0f.toPx(context)
                            )
                            lineTo(
                                x = x.toPx(context),
                                y = curveStartY
                            )
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
                                width = strokeWidthPx,
                            )
                        )
                    }
                }

                // new depth line coming out of avatar
                if (depthLinesUiState.depthLines.contains(depthLinesUiState.postDepth)) {
                    val drawDepth = postDepth - startingDepth + 1
                    val x = (spacingWidth * drawDepth).toFloat() + 1
                    val xPx = x.toPx(context)
                    if (depthLinesUiState.showViewMoreRepliesText) {
                        // goes to the view more replies text
                        val viewMoreRepliesTextCenterY = height - 32f.toPx(context)
                        val curveStartY =
                            viewMoreRepliesTextCenterY - spacingWidth.toFloat().toPx(context)

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
                                width = strokeWidthPx,
                            )
                        )
                    } else if (depthLinesUiState.expandRepliesButtonUiState == ExpandRepliesButtonUiState.PLUS) {
                        // goes to the plus icon
                        drawLine(
                            color = lineColor,
                            start = Offset(
                                x = xPx,
                                y = avatarCenterYPx,
                            ),
                            end = Offset(
                                x = xPx,
                                y = plusIconCenterYPx,
                            ),
                            strokeWidth = strokeWidthPx,
                            cap = StrokeCap.Round
                        )
                    } else {
                        // goes to the next post
                        drawLine(
                            color = lineColor,
                            start = Offset(
                                x = xPx,
                                y = avatarCenterYPx,
                            ),
                            end = Offset(
                                x = xPx,
                                y = height,
                            ),
                            strokeWidth = strokeWidthPx,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DepthLinesExpandButton(
    postCardUiState: PostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Box {
        val expandRepliesButtonUiState =
            postCardUiState.depthLinesUiState?.expandRepliesButtonUiState ?: return

        if (expandRepliesButtonUiState != ExpandRepliesButtonUiState.HIDDEN) {
            IconButton(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(20.dp),
                onClick = { postCardInteractions.onHideRepliesClicked(postCardUiState.statusId) }
            ) {
                Icon(
                    modifier = Modifier
                        .background(FfTheme.colors.layer1),
                    painter = if (expandRepliesButtonUiState == ExpandRepliesButtonUiState.PLUS) {
                        FfIcons.plusCircle()
                    } else {
                        FfIcons.minusCircle()
                    },
                    contentDescription = null
                )
            }
        }
    }
}