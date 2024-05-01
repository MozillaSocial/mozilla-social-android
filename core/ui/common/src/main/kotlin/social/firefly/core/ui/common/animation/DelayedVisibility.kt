package social.firefly.core.ui.common.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DelayedVisibility(
    key: Any = Unit,
    visible: Boolean = true,
    delay: Int = 200,
    content: @Composable () -> Unit,
) {
    var innerVisible by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visible = innerVisible,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 0, delayMillis = delay)
        ) + fadeIn(
            animationSpec = tween(delayMillis = delay)
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 150)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 50)
        ),
    ) {
        content()
    }
    LaunchedEffect(key1 = key) {
        innerVisible = visible
    }
}