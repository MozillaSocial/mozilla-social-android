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
        primary = FirefoxColor.Violet60,
        onPrimary = FirefoxColor.Violet10,
        primaryContainer = FirefoxColor.Violet10,
        onPrimaryContainer = FirefoxColor.Violet80,
        inversePrimary = FirefoxColor.DarkGrey90,
        secondary = FirefoxColor.LightGrey40,
        onSecondary = FirefoxColor.DarkGrey05,
        secondaryContainer = FirefoxColor.LightGrey40,
        onSecondaryContainer = FirefoxColor.DarkGrey05,
        tertiary = FirefoxColor.Violet60,
        onTertiary = FirefoxColor.LightGrey05,
        tertiaryContainer = FirefoxColor.Violet60,
        onTertiaryContainer = FirefoxColor.LightGrey05,
        background = FirefoxColor.White,
        onBackground = FirefoxColor.DarkGrey90,
        surface = FirefoxColor.LightGrey20,
        onSurface = FirefoxColor.DarkGrey90,
        surfaceVariant = FirefoxColor.LightGrey20,
        onSurfaceVariant = FirefoxColor.Ink20,
        surfaceTint = FirefoxColor.Violet05,
        inverseSurface = FirefoxColor.DarkGrey05,
        inverseOnSurface = FirefoxColor.LightGrey05,
        error = FirefoxColor.Red70,
        onError = FirefoxColor.LightGrey05,
        errorContainer = FirefoxColor.Red70,
        onErrorContainer = FirefoxColor.LightGrey05,
        outline = FirefoxColor.Violet60,
        outlineVariant = FirefoxColor.LightGrey30,
        scrim = FirefoxColor.DarkGrey30,
    )

/**
 * This is for compatability purposes only- please use [FfColors] instead
 */
internal val MaterialDarkColorScheme =
    darkColorScheme(
        primary = FirefoxColor.Violet90,
        onPrimary = FirefoxColor.Violet10,
        primaryContainer = FirefoxColor.Violet80,
        onPrimaryContainer = FirefoxColor.Violet20,
        inversePrimary = FirefoxColor.LightGrey05,
        secondary = FirefoxColor.DarkGrey80,
        onSecondary = FirefoxColor.LightGrey30,
        secondaryContainer = FirefoxColor.DarkGrey80,
        onSecondaryContainer = FirefoxColor.LightGrey30,
        tertiary = FirefoxColor.Violet60,
        onTertiary = FirefoxColor.LightGrey30,
        tertiaryContainer = FirefoxColor.Violet60,
        onTertiaryContainer = FirefoxColor.LightGrey30,
        background = FirefoxColor.DarkGrey60,
        onBackground = FirefoxColor.LightGrey30,
        surface = FirefoxColor.DarkGrey40,
        onSurface = FirefoxColor.LightGrey30,
        surfaceVariant = FirefoxColor.DarkGrey80,
        onSurfaceVariant = FirefoxColor.Violet20,
        surfaceTint = FirefoxColor.Violet80,
        inverseSurface = FirefoxColor.LightGrey10,
        inverseOnSurface = FirefoxColor.DarkGrey60,
        error = FirefoxColor.Red50,
        onError = FirefoxColor.LightGrey10,
        errorContainer = FirefoxColor.Red70,
        onErrorContainer = FirefoxColor.LightGrey10,
        outline = FirefoxColor.Violet60,
        outlineVariant = FirefoxColor.DarkGrey80,
        scrim = FirefoxColor.DarkGrey90,
    )
