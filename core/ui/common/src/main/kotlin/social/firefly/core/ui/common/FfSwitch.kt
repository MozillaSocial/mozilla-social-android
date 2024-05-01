package social.firefly.core.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckChanged: () -> Unit,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = FfSwitchPrimaryDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckChanged() },
        modifier = modifier,
        thumbContent = thumbContent,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

object FfSwitchPrimaryDefaults {
    @Composable
    fun colors(): SwitchColors =
        SwitchDefaults.colors(
            checkedThumbColor = FfTheme.colors.layer1,
            checkedTrackColor = FfTheme.colors.layerActionPrimaryEnabled,
            uncheckedThumbColor = FfTheme.colors.layerActionDisabled,
            uncheckedBorderColor = FfTheme.colors.layerActionDisabled,
            disabledCheckedTrackColor = FfTheme.colors.layerActionDisabled,
            disabledUncheckedThumbColor = FfTheme.colors.layer2,
            disabledUncheckedTrackColor = FfTheme.colors.textActionDisabled,
            disabledUncheckedBorderColor = FfTheme.colors.textActionDisabled,
        )
}

@Preview
@Composable
private fun SwitchPreview() {
    Column {
        FfTheme {
            FfSwitch(
                checked = true,
                onCheckChanged = { /*TODO*/ },
            )
            FfSwitch(
                checked = false,
                onCheckChanged = { /*TODO*/ },
            )
            FfSwitch(
                checked = true,
                enabled = false,
                onCheckChanged = { /*TODO*/ },
            )
            FfSwitch(
                checked = false,
                enabled = false,
                onCheckChanged = { /*TODO*/ },
            )
        }
    }
}
