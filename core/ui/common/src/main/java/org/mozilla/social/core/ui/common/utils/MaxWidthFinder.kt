package org.mozilla.social.core.ui.common.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A composable function that returns the max width of the parent.
 */
@Composable
fun getMaxWidth(): Dp {
    val density = LocalDensity.current
    var maxWidth by remember { mutableStateOf(0.dp) }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    with(density) {
                        maxWidth = it.width.toDp()
                    }
                },
    )
    return maxWidth
}

@Composable
fun getMaxWidthInt(): Int {
    var maxWidth by remember { mutableIntStateOf(0) }

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .onSizeChanged {
                maxWidth = it.width
            },
    )
    return maxWidth
}
