package social.firefly.core.ui.common.button

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = FfButtonDefaults.shape,
    theme: FfButtonTheme = FfButtonPrimaryDefaults,
    contentPadding: PaddingValues = FfButtonContentPadding.default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    FfButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = theme.colors(),
        elevation = theme.elevation(),
        border = theme.border(),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun FfButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = FfButtonDefaults.shape,
    colors: ButtonColors = FfButtonPrimaryDefaults.colors(),
    elevation: ButtonElevation? = FfButtonDefaults.buttonElevation(),
    border: BorderStroke? = FfButtonPrimaryDefaults.border(),
    contentPadding: PaddingValues = FfButtonContentPadding.default,
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
fun FfButtonSecondary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = FfButtonDefaults.shape,
    colors: ButtonColors = FfButtonSecondaryDefaults.colors(),
    elevation: ButtonElevation? = FfButtonDefaults.buttonElevation(),
    border: BorderStroke? = FfButtonSecondaryDefaults.border(),
    contentPadding: PaddingValues = FfButtonContentPadding.default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    FfButton(
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

object FfButtonPrimaryDefaults : FfButtonTheme {
    @Composable
    override fun colors(): ButtonColors =
        FfButtonDefaults.buttonColors(
            containerColor = FfTheme.colors.layerActionPrimaryEnabled,
            contentColor = FfTheme.colors.textActionPrimary,
            disabledContainerColor = FfTheme.colors.layerActionDisabled,
            disabledContentColor = FfTheme.colors.textActionPrimary,
        )

    @Composable
    override fun border(): BorderStroke? = null
}

object FfButtonSecondaryDefaults : FfButtonTheme {
    @Composable
    override fun colors(): ButtonColors =
        FfButtonDefaults.buttonColors(
            containerColor = FfTheme.colors.layer1,
            contentColor = FfTheme.colors.textActionSecondary,
            disabledContainerColor = FfTheme.colors.layer2,
            disabledContentColor = FfTheme.colors.textActionDisabled,
        )

    @Composable
    override fun border(): BorderStroke = BorderStroke(
        width = 1.dp,
        brush = SolidColor(FfTheme.colors.borderPrimary),
    )
}

interface FfButtonTheme {
    @Composable
    fun colors(): ButtonColors

    @Composable
    fun border(): BorderStroke?

    @Composable
    fun elevation(): ButtonElevation? = FfButtonDefaults.buttonElevation()
}

object FfButtonContentPadding {
    val default = PaddingValues(
        horizontal = 24.dp,
        vertical = 14.dp,
    )
    val small = PaddingValues(
        horizontal = 20.dp,
        vertical = 10.dp,
    )
}

object FfButtonDefaults {

    val shape: Shape @Composable get() = ButtonDefaults.shape

    @Composable
    fun buttonElevation() = ButtonDefaults.buttonElevation()

    @Composable
    fun buttonColors(
        containerColor: Color,
        contentColor: Color,
        disabledContainerColor: Color,
        disabledContentColor: Color,
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}

@Preview
@Composable
private fun ButtonPreview() {
    Column {
        FfTheme {
            FfButton(onClick = { /*TODO*/ }) {
                Text(text = "Primary")
            }
            FfButton(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Primary Disabled")
            }
            FfButtonSecondary(onClick = { /*TODO*/ }) {
                Text(text = "Secondary")
            }
            FfButtonSecondary(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Secondary Disabled")
            }
        }

        FfTheme(
            darkTheme = true,
        ) {
            FfButton(onClick = { /*TODO*/ }) {
                Text(text = "Dark mode Primary")
            }
            FfButton(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Dark mode Primary Disabled")
            }
            FfButtonSecondary(onClick = { /*TODO*/ }) {
                Text(text = "Dark mode Secondary")
            }
            FfButtonSecondary(
                enabled = false,
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Dark mode Secondary Disabled")
            }
        }
    }
}
