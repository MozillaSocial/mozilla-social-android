package org.mozilla.social.core.ui.common.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.theme.MoSoTheme

sealed class ToggleButtonState {
    @Composable
    abstract fun colors(): ButtonColors

    @Composable
    open fun borderStroke(): BorderStroke? = null

    data object Primary : ToggleButtonState() {
        @Composable
        override fun colors(): ButtonColors = MoSoButtonPrimaryDefaults.colors()
    }

    data object Secondary : ToggleButtonState() {
        @Composable
        override fun colors(): ButtonColors = MoSoButtonSecondaryDefaults.colors()

        @Composable
        override fun borderStroke(): BorderStroke {
            return BorderStroke(
                width = 1.dp,
                brush = SolidColor(MoSoTheme.colors.borderPrimary),
            )
        }
    }
}

operator fun ToggleButtonState.not() = when (this) {
    ToggleButtonState.Primary -> ToggleButtonState.Secondary
    ToggleButtonState.Secondary -> ToggleButtonState.Primary
}