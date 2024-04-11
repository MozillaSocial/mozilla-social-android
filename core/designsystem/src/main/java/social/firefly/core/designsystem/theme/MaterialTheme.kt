package social.firefly.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Material color schemes for compatability with our [FfTheme]
 */
@Preview
@Composable
private fun ColorSchemePreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
            LightPalette()
            Spacer(Modifier.height(14.dp))
            DarkPalette()
        }
    }
}

@Composable
private fun RowScope.LightPalette() {
    Column(
        Modifier.weight(1f),
    ) {
        Text(text = "Light Scheme", style = Typography.headlineMedium)
        ColorSchemePalette(colorScheme = MaterialLightColorScheme)
    }
}

@Composable
private fun RowScope.DarkPalette() {
    Column(
        Modifier.weight(1f),
    ) {
        Text(text = "Dark Scheme", style = Typography.headlineMedium)
        ColorSchemePalette(colorScheme = MaterialDarkColorScheme)
    }
}

@Composable
private fun ColorSchemePalette(colorScheme: ColorScheme) {
    Column(
        modifier = Modifier.background(colorScheme.background),
    ) {
        TestBox(
            text = "primary",
            textColor = colorScheme.onPrimary,
            backgroundColor = colorScheme.primary,
        )
        TestBox(
            text = "primary container",
            textColor = colorScheme.onPrimaryContainer,
            backgroundColor = colorScheme.primaryContainer,
        )
        TestBox(
            text = "secondary",
            textColor = colorScheme.onSecondary,
            backgroundColor = colorScheme.secondary,
        )
        TestBox(
            text = "secondary container",
            textColor = colorScheme.onSecondaryContainer,
            backgroundColor = colorScheme.secondaryContainer,
        )
        TestBox(
            text = "tertiary",
            textColor = colorScheme.onTertiary,
            backgroundColor = colorScheme.tertiary,
        )
        TestBox(
            text = "tertiary container",
            textColor = colorScheme.onTertiaryContainer,
            backgroundColor = colorScheme.tertiaryContainer,
        )
        TestBox(
            text = "background",
            textColor = colorScheme.onBackground,
            backgroundColor = colorScheme.background,
        )
        TestBox(
            text = "surface",
            textColor = colorScheme.onSurface,
            backgroundColor = colorScheme.surface,
        )
        TestBox(
            text = "surface variant",
            textColor = colorScheme.onSurfaceVariant,
            backgroundColor = colorScheme.surfaceVariant,
        )
        TestBox(
            textColor = colorScheme.onSurfaceVariant,
            backgroundColor = colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun TestBox(
    modifier: Modifier = Modifier,
    text: String = "some text",
    textColor: Color,
    backgroundColor: Color,
) {
    Box(
        modifier =
        modifier
            .padding(4.dp)
            .background(color = backgroundColor),
    ) {
        Text(modifier = Modifier.padding(16.dp), text = text, color = textColor)
    }
}

/**
 * This is for compatability purposes only- please use [FfColors] instead
 */
internal val MaterialLightColorScheme =
    lightColorScheme(
        primary = ColorPalette.Violet60,
        onPrimary = ColorPalette.Violet10,
        primaryContainer = ColorPalette.Violet10,
        onPrimaryContainer = ColorPalette.Violet80,
        inversePrimary = ColorPalette.DarkGrey90,
        secondary = ColorPalette.LightGrey40,
        onSecondary = ColorPalette.DarkGrey05,
        secondaryContainer = ColorPalette.LightGrey40,
        onSecondaryContainer = ColorPalette.DarkGrey05,
        tertiary = ColorPalette.Violet60,
        onTertiary = ColorPalette.LightGrey05,
        tertiaryContainer = ColorPalette.Violet60,
        onTertiaryContainer = ColorPalette.LightGrey05,
        background = ColorPalette.White,
        onBackground = ColorPalette.DarkGrey90,
        surface = ColorPalette.LightGrey20,
        onSurface = ColorPalette.DarkGrey90,
        surfaceVariant = ColorPalette.LightGrey20,
        onSurfaceVariant = ColorPalette.Ink20,
        surfaceTint = ColorPalette.Violet05,
        inverseSurface = ColorPalette.DarkGrey05,
        inverseOnSurface = ColorPalette.LightGrey05,
        error = ColorPalette.Red70,
        onError = ColorPalette.LightGrey05,
        errorContainer = ColorPalette.Red70,
        onErrorContainer = ColorPalette.LightGrey05,
        outline = ColorPalette.Violet60,
        outlineVariant = ColorPalette.LightGrey30,
        scrim = ColorPalette.DarkGrey30,
    )

/**
 * This is for compatability purposes only- please use [FfColors] instead
 */
internal val MaterialDarkColorScheme =
    darkColorScheme(
        primary = ColorPalette.Violet90,
        onPrimary = ColorPalette.Violet10,
        primaryContainer = ColorPalette.Violet80,
        onPrimaryContainer = ColorPalette.Violet20,
        inversePrimary = ColorPalette.LightGrey05,
        secondary = ColorPalette.DarkGrey80,
        onSecondary = ColorPalette.LightGrey30,
        secondaryContainer = ColorPalette.DarkGrey80,
        onSecondaryContainer = ColorPalette.LightGrey30,
        tertiary = ColorPalette.Violet60,
        onTertiary = ColorPalette.LightGrey30,
        tertiaryContainer = ColorPalette.Violet60,
        onTertiaryContainer = ColorPalette.LightGrey30,
        background = ColorPalette.DarkGrey60,
        onBackground = ColorPalette.LightGrey30,
        surface = ColorPalette.DarkGrey40,
        onSurface = ColorPalette.LightGrey30,
        surfaceVariant = ColorPalette.DarkGrey80,
        onSurfaceVariant = ColorPalette.Violet20,
        surfaceTint = ColorPalette.Violet80,
        inverseSurface = ColorPalette.LightGrey10,
        inverseOnSurface = ColorPalette.DarkGrey60,
        error = ColorPalette.Red50,
        onError = ColorPalette.LightGrey10,
        errorContainer = ColorPalette.Red70,
        onErrorContainer = ColorPalette.LightGrey10,
        outline = ColorPalette.Violet60,
        outlineVariant = ColorPalette.DarkGrey80,
        scrim = ColorPalette.DarkGrey90,
    )
