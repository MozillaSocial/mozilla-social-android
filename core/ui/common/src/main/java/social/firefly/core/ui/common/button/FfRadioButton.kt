package social.firefly.core.ui.common.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfSurface

@Composable
fun FfRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = FfRadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

object FfRadioButtonDefaults {
    @Composable
    fun colors(): RadioButtonColors =
        RadioButtonDefaults.colors(
            selectedColor = FfTheme.colors.iconActionActive,
            unselectedColor = FfTheme.colors.iconActionDisabled,
            disabledSelectedColor = FfTheme.colors.iconActionActive,
            disabledUnselectedColor = FfTheme.colors.iconActionDisabled,
        )
}

@Preview
@Composable
private fun Preview() {
    FfTheme {
        FfSurface {
            FfRadioButton(selected = true, onClick = null)
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
            FfRadioButton(selected = true, onClick = null)
        }
    }
}
