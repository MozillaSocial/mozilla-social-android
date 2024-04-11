package social.firefly.core.ui.common.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Expands content in and out.  If the content is not visible, it will be completely hidden
 */
@Composable
fun ExpandingAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean,
    label: String = "AnimatedVisibility",
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter =
        expandVertically(
            expandFrom = Alignment.Top,
        ) { 0 } + fadeIn(),
        exit =
        shrinkVertically(
            shrinkTowards = Alignment.Top,
        ) {
            0
        } + fadeOut(),
        label = label,
    ) {
        content()
    }
}
