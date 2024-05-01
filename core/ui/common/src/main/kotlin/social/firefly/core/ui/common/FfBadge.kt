package social.firefly.core.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import social.firefly.core.designsystem.theme.FfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FfBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = FfTheme.colors.actionPrimary,
    contentColor: Color = FfTheme.colors.textActionPrimary,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}
