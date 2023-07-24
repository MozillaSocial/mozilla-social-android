package org.mozilla.social.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FirefoxColor.Violet70,
    onPrimary = FirefoxColor.LightGrey10,
    primaryContainer = FirefoxColor.LightGrey10,
    onPrimaryContainer = FirefoxColor.DarkGrey90,
    inversePrimary = FirefoxColor.DarkGrey90,
    secondary = FirefoxColor.LightGrey40,
    onSecondary = FirefoxColor.DarkGrey05,
    secondaryContainer = FirefoxColor.LightGrey40,
    onSecondaryContainer = FirefoxColor.DarkGrey05,
    tertiary = FirefoxColor.Violet60,
    onTertiary = FirefoxColor.LightGrey05,
    tertiaryContainer = FirefoxColor.Violet60,
    onTertiaryContainer = FirefoxColor.LightGrey05,
    background = FirefoxColor.LightGrey10,
    onBackground = FirefoxColor.DarkGrey90,
    surface = FirefoxColor.LightGrey10,
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

private val DarkColorScheme = darkColorScheme(
    primary = FirefoxColor.Violet80,
    onPrimary = FirefoxColor.LightGrey05,
    primaryContainer = FirefoxColor.DarkGrey60,
    onPrimaryContainer = FirefoxColor.LightGrey05,
    inversePrimary = FirefoxColor.LightGrey05,
    secondary = FirefoxColor.DarkGrey80,
    onSecondary = FirefoxColor.LightGrey30,
    secondaryContainer = FirefoxColor.DarkGrey80,
    onSecondaryContainer = FirefoxColor.LightGrey30,
    tertiary = FirefoxColor.Violet60,
    onTertiary = FirefoxColor.LightGrey30,
    tertiaryContainer = FirefoxColor.Violet60,
    onTertiaryContainer = FirefoxColor.LightGrey30,
    background = FirefoxColor.DarkGrey80,
    onBackground = FirefoxColor.LightGrey30,
    surface = FirefoxColor.DarkGrey60,
    onSurface = FirefoxColor.LightGrey30,
    surfaceVariant = FirefoxColor.DarkGrey80,
    onSurfaceVariant = FirefoxColor.Violet60,
    surfaceTint = FirefoxColor.Violet80,
    inverseSurface = FirefoxColor.LightGrey10,
    inverseOnSurface = FirefoxColor.DarkGrey60,
    error = FirefoxColor.Red70,
    onError = FirefoxColor.LightGrey10,
    errorContainer = FirefoxColor.Red70,
    onErrorContainer = FirefoxColor.LightGrey10,
    outline = FirefoxColor.Violet60,
    outlineVariant = FirefoxColor.DarkGrey80,
    scrim = FirefoxColor.DarkGrey90,
)

@Composable
fun MozillaSocialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.inverseSurface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}