package org.mozilla.social.feature.settings.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
internal fun SettingsSection(
    title: String,
    subtitle: String? = null,
    iconPainter: Painter? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(MoSoSpacing.md)
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
                modifier = Modifier.size(MoSoIcons.Sizes.normal),
                painter = iconPainter,
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier.padding(MoSoSpacing.sm),
        ) {
            Text(
                text = title,
                style = MoSoTheme.typography.labelMedium,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MoSoTheme.typography.bodySmall,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = MoSoIcons.chevronRight(), contentDescription = null)
    }
}