package social.firefly.core.ui.common.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = FfTheme.colors.borderPrimary,
    thickness: Dp = 1.dp,
) {
    Box(
        modifier =
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color),
    )
}

@Preview
@Composable
private fun VerticalDividerPreview() {
    FfTheme {
        FfVerticalDivider()
    }
}
