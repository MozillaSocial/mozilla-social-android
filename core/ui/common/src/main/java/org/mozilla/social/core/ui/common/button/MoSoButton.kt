package org.mozilla.social.core.ui.common.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = MoSoButtonPrimaryDefaults.colors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun MoSoButtonSecondary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = MoSoButtonSecondaryDefaults.colors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? =
        BorderStroke(
            width = 1.dp,
            brush = SolidColor(MoSoTheme.colors.borderPrimary),
        ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    MoSoButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

object MoSoButtonPrimaryDefaults {
    @Composable
    fun colors(): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = MoSoTheme.colors.layerActionPrimaryEnabled,
            contentColor = MoSoTheme.colors.textActionPrimary,
            disabledContainerColor = MoSoTheme.colors.layerActionDisabled,
            disabledContentColor = MoSoTheme.colors.textActionPrimary,
        )
}

object MoSoButtonSecondaryDefaults {
    @Composable
    fun colors(): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = MoSoTheme.colors.layer1,
            contentColor = MoSoTheme.colors.textActionSecondary,
            disabledContainerColor = MoSoTheme.colors.layer2,
            disabledContentColor = MoSoTheme.colors.textActionDisabled,
        )
}

@Preview
@Composable
private fun ButtonPreview() {
    Column {
        MoSoTheme {
            MoSoButton(onClick = { /*TODO*/ }) {
                Text(text = "Primary")
            }
            MoSoButton(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Primary Disabled")
            }
            MoSoButtonSecondary(onClick = { /*TODO*/ }) {
                Text(text = "Secondary")
            }
            MoSoButtonSecondary(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Secondary Disabled")
            }
        }

        MoSoTheme(
            darkTheme = true,
        ) {
            MoSoButton(onClick = { /*TODO*/ }) {
                Text(text = "Dark mode Primary")
            }
            MoSoButton(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Dark mode Primary Disabled")
            }
            MoSoButtonSecondary(onClick = { /*TODO*/ }) {
                Text(text = "Dark mode Secondary")
            }
            MoSoButtonSecondary(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Dark mode Secondary Disabled")
            }
        }
    }
}
