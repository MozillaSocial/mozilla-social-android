package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckChanged: () -> Unit,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = MoSoSwitchPrimaryDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckChanged() },
        modifier = modifier,
        thumbContent = thumbContent,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource
    )
}

object MoSoSwitchPrimaryDefaults {
    @Composable
    fun colors(): SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = MoSoTheme.colors.layer1,
        checkedTrackColor = MoSoTheme.colors.layerActionPrimaryEnabled,
        uncheckedThumbColor = MoSoTheme.colors.layerActionDisabled,
        uncheckedBorderColor = MoSoTheme.colors.layerActionDisabled,
        disabledCheckedTrackColor = MoSoTheme.colors.layerActionDisabled,
        disabledUncheckedThumbColor = MoSoTheme.colors.layer2,
        disabledUncheckedTrackColor = MoSoTheme.colors.textActionDisabled,
        disabledUncheckedBorderColor = MoSoTheme.colors.textActionDisabled
    )
}

@Preview
@Composable
private fun SwitchPreview() {
    Column {
        MoSoTheme {
            MoSoSwitch(
                checked = true,
                onCheckChanged = { /*TODO*/ }
            )
            MoSoSwitch(
                checked = false,
                onCheckChanged = { /*TODO*/ }
            )
            MoSoSwitch(
                checked = true,
                enabled = false,
                onCheckChanged = { /*TODO*/ }
            )
            MoSoSwitch(
                checked = false,
                enabled = false,
                onCheckChanged = { /*TODO*/ }
            )
        }
    }
}