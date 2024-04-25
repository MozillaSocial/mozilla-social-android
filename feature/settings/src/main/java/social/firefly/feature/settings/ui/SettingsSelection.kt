package social.firefly.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
internal fun SettingsSelection(
    title: String,
    description: String? = null,
    selector: @Composable () -> Unit,
) {
    FfSurface(
        color = Color.Transparent,
        modifier =
        Modifier
            .fillMaxWidth(),
    ) {
        Column {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                selector()
            }
            FfDivider()
        }
    }
}

@Preview
@Composable
private fun SettingsSelectionPreview() {
    PreviewTheme {
        SettingsSelection(title = "hello hfdkjsaj hfkdjsa fklhjd jakfhakj hfi ahfkjsdah flksdjf lksaj fksa hsdh") {
            FfButton(onClick = { }) {

            }
        }
    }
}
