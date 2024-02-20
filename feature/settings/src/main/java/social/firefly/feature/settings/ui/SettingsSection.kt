package social.firefly.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme

@Composable
internal fun SettingsSection(
    title: String,
    subtitle: String? = null,
    iconPainter: Painter? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier =
        Modifier
            .padding(FfSpacing.md)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
            .semantics(mergeDescendants = true) {
                isTraversalGroup = true
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        iconPainter?.let {
            Icon(
                modifier = Modifier.size(FfIcons.Sizes.normal),
                painter = iconPainter,
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier.padding(FfSpacing.sm),
        ) {
            Text(
                text = title,
                style = FfTheme.typography.labelMedium,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = FfTheme.typography.bodySmall,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = FfIcons.chevronRight(), contentDescription = null)
    }
}
