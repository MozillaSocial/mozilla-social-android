package social.firefly.core.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = FfCheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

object FfCheckboxDefaults {
    @Composable
    fun colors(): CheckboxColors =
        CheckboxDefaults.colors(
            checkedColor = FfTheme.colors.iconActionActive,
            uncheckedColor = FfTheme.colors.borderInputEnabled,
            checkmarkColor = FfTheme.colors.textActionPrimary,
            disabledCheckedColor = FfTheme.colors.iconActionActive,
            disabledUncheckedColor = FfTheme.colors.iconActionDisabled,
            disabledIndeterminateColor = FfTheme.colors.textActionPrimary,
        )
}

@Preview
@Composable
private fun Preview() {
    FfTheme {
        FfSurface {
            Column {
                FfCheckBox(checked = true, onCheckedChange = {})
                FfCheckBox(checked = false, onCheckedChange = {})
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDarkMode() {
    FfTheme(
        darkTheme = true,
    ) {
        FfSurface {
            Column {
                FfCheckBox(checked = true, onCheckedChange = {})
                FfCheckBox(checked = false, onCheckedChange = {})
            }
        }
    }
}
