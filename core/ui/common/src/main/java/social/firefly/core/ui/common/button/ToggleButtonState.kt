package social.firefly.core.ui.common.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

sealed class ToggleButtonState {
    @Composable
    abstract fun colors(): ButtonColors

    @Composable
    open fun borderStroke(): BorderStroke? = null

    data object Primary : ToggleButtonState() {
        @Composable
        override fun colors(): ButtonColors = FfButtonPrimaryDefaults.colors()
    }

    data object Secondary : ToggleButtonState() {
        @Composable
        override fun colors(): ButtonColors = FfButtonSecondaryDefaults.colors()

        @Composable
        override fun borderStroke(): BorderStroke {
            return BorderStroke(
                width = 1.dp,
                brush = SolidColor(FfTheme.colors.borderPrimary),
            )
        }
    }
}

operator fun ToggleButtonState.not() = when (this) {
    ToggleButtonState.Primary -> ToggleButtonState.Secondary
    ToggleButtonState.Secondary -> ToggleButtonState.Primary
}
