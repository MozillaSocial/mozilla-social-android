package social.firefly.core.ui.common.divider

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = FfDividerDefaults.Thickness,
    color: Color = FfDividerDefaults.color,
) {
    Divider(modifier, thickness, color)
}

object FfDividerDefaults {
    /** Default thickness of a divider. */
    val Thickness: Dp = 1.dp

    /** Default color of a divider. */
    val color: Color @Composable get() = FfTheme.colors.borderPrimary
}
