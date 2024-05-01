package social.firefly.core.ui.common.tabs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContentColor: Color = FfTheme.colors.textLink,
    unselectedContentColor: Color = FfTheme.colors.textPrimary,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 40.dp),
        enabled = enabled,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        interactionSource = interactionSource,
        content = {
            Column(modifier = Modifier.padding(horizontal = contentPadding)) {
                content()
            }
        },
    )
}
