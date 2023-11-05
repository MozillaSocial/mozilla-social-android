package org.mozilla.social.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar

@Composable
internal fun SettingsColumn(
    title: String,
    modifier: Modifier = Modifier.padding(MoSoSpacing.sm),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        MoSoCloseableTopAppBar(title = title)
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
        ) { content() }
    }
}