package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = MoSoTheme.colors.actionPrimary,
    contentColor: Color = MoSoTheme.colors.textActionPrimary,
    content: @Composable (RowScope.() -> Unit)? = null,
) {
    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}
