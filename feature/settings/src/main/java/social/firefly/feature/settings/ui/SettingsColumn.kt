package social.firefly.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar

@Composable
internal fun SettingsColumn(
    title: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        FfCloseableTopAppBar(title = title)
        Column(
            modifier = modifier
                .padding(FfSpacing.sm),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
        ) { content() }
    }
}
