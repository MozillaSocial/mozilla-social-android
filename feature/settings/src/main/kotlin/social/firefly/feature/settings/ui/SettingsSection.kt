package social.firefly.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
internal fun SettingsSection(
    title: String,
    subtitle: String? = null,
    iconPainter: Painter? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = FfSpacing.md)
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
            Spacer(modifier = Modifier.width(FfSpacing.sm))
        }
        Column(
            modifier = Modifier.padding(end = FfSpacing.sm),
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

@Preview
@Composable
private fun SettingsSectionPreview() {
    PreviewTheme {
        SettingsSection(
            title = "title",
            subtitle = "subtitle",
            iconPainter = FfIcons.bookmark()
        ) {}
    }
}

@Preview
@Composable
private fun SettingsSectionPreviewNoIcon() {
    PreviewTheme {
        SettingsSection(
            title = "title",
            subtitle = "subtitle",
        ) {}
    }
}
