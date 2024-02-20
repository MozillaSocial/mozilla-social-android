package social.firefly.core.ui.common.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme

/**
 * A toggle button which automatically toggles between a primary and secondary state
 * (primary being the more visually prominent state)
 */
@Composable
fun FfAutomaticToggleButton(
    onClick: (ToggleButtonState) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    initialToggleState: ToggleButtonState = ToggleButtonState.Primary,
    shape: Shape = ButtonDefaults.shape,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    var toggleState by remember { mutableStateOf(initialToggleState) }

    FfToggleButton(
        onClick = {
            toggleState = !toggleState
            onClick(toggleState)
        },
        modifier = modifier,
        enabled = enabled,
        toggleState = toggleState,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * A toggle button which toggles between a primary and secondary state (primary being the more
 * visually prominent state)
 */
@Composable
fun FfToggleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    toggleState: ToggleButtonState = ToggleButtonState.Primary,
    shape: Shape = ButtonDefaults.shape,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {

    FfButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = toggleState.colors(),
        elevation = elevation,
        border = toggleState.borderStroke(),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

@Preview
@Composable
private fun ToggleButtonPreview() {
    PreviewTheme {
        var state: ToggleButtonState by remember { mutableStateOf(ToggleButtonState.Primary) }

        FfAutomaticToggleButton(
            onClick = { state = it }) {
            SmallTextLabel(
                text = when (state) {
                    ToggleButtonState.Primary -> {
                        "Primary"
                    }

                    ToggleButtonState.Secondary -> {
                        "Secondary"
                    }
                }
            )
        }
    }
}
