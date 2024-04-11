package social.firefly.feature.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.FfSwitch
import social.firefly.core.ui.common.divider.FfDivider

@Suppress("LongParameterList", "MagicNumber")
@Composable
internal fun SettingsSwitch(
    @StringRes title: Int,
    @StringRes description: Int? = null,
    checked: Boolean,
    onCheckedChanged: () -> Unit,
) {
    FfSurface(
        color = Color.Transparent,
        modifier =
        Modifier
            .fillMaxWidth(),
        onClick = onCheckedChanged,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Column(
                        modifier =
                        Modifier
                            .weight(9f)
                            .padding(bottom = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = title),
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                            )
                        }
                        if (description != null) {
                            Text(
                                text = stringResource(id = description),
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    FfSwitch(
                        checked = checked,
                        onCheckChanged = { onCheckedChanged() },
                        modifier =
                        Modifier
                            .padding(end = 16.dp),
                    )
                }
            }
            FfDivider()
        }
    }
}
