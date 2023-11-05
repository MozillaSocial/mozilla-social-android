package org.mozilla.social.feature.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Suppress("MagicNumber")
@Composable
fun SettingsGroup(
    @StringRes name: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp)
    ) {
        Text(text = stringResource(id = name), style = MoSoTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}