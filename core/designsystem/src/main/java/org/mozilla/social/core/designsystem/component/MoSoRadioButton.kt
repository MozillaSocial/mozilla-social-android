package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = MoSoRadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource
    )
}

object MoSoRadioButtonDefaults {
    @Composable
    fun colors() : RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = MoSoTheme.colors.iconActionActive,
        unselectedColor = MoSoTheme.colors.iconActionDisabled,
        disabledSelectedColor = MoSoTheme.colors.iconActionActive,
        disabledUnselectedColor = MoSoTheme.colors.iconActionDisabled,
    )
}

@Preview
@Composable
private fun Preview() {
    MoSoTheme {
        MoSoSurface {
            MoSoRadioButton(selected = true, onClick = null)
        }
    }
}

@Preview
@Composable
private fun PreviewDarkMode() {
    MoSoTheme(
        darkTheme = true
    ) {
        MoSoSurface {
            MoSoRadioButton(selected = true, onClick = null)
        }
    }
}