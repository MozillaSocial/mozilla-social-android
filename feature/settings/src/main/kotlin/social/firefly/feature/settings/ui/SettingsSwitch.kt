package social.firefly.feature.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.ui.common.FfSwitch
import social.firefly.core.ui.common.utils.PreviewTheme

@Suppress("LongParameterList", "MagicNumber")
@Composable
internal fun SettingsSwitch(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChanged: () -> Unit,
) {
    SettingsSelection(
        title = title,
        description = description,
    ) {
        FfSwitch(
            checked = checked,
            onCheckChanged = { onCheckedChanged() },
        )
    }
}

@Preview
@Composable
private fun SettingsSwitchPreview() {
    PreviewTheme {
        SettingsSwitch(
            title = "hello this is settings fjdklsafjdlaskfj kldsajf lksad fjklds jfkdsl jfkds",
            checked = false,
        ) {

        }
    }
}
