/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package social.firefly.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun FfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors =
        when (darkTheme) {
            false -> lightColorPalette
            true -> darkColorPalette
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = if (darkTheme) MaterialDarkColorScheme else MaterialLightColorScheme,
    ) {
        ProvideFfColors(colors = colors) {
            val textSelectionColors = TextSelectionColors(
                handleColor = FfTheme.colors.iconAccent,
                backgroundColor = FfTheme.colors.layer2,
            )

            CompositionLocalProvider(
                LocalContentColor provides FfTheme.colors.textPrimary,
                LocalTextStyle provides FfTheme.typography.bodyMedium,
                LocalTextSelectionColors provides textSelectionColors,
                content = content,
            )
        }
    }
}

object FfTheme {
    val colors: FfColors
        @Composable
        get() = localFfColors.current

    val typography: FfTypography
        get() = defaultTypography
}

private val darkColorPalette =
    FfColors(
        layer1 = ColorPalette.DarkGrey70,
        layer2 = ColorPalette.DarkGrey40,
        layerAccent = ColorPalette.Violet60,
        layerActionPrimaryEnabled = ColorPalette.Violet60,
        layerActionSecondaryEnabled = ColorPalette.White,
        layerActionDisabled = ColorPalette.LightGrey70,
        layerActionWarning = ColorPalette.Yellow40,
        scrim = ColorPalette.DarkGrey90,
        actionPrimary = ColorPalette.Violet60,
        actionSecondary = ColorPalette.LightGrey30,
        actionOverlay = ColorPalette.DarkGrey90,
        textPrimary = ColorPalette.LightGrey05,
        textSecondary = ColorPalette.LightGrey40,
        textLink = ColorPalette.Violet20,
        textWarning = ColorPalette.Red20,
        textActionPrimary = ColorPalette.LightGrey05,
        textActionSecondary = ColorPalette.White,
        textActionDisabled = ColorPalette.LightGrey90,
        iconPrimary = ColorPalette.LightGrey05,
        iconSecondary = ColorPalette.LightGrey40,
        iconAccent = ColorPalette.Violet30,
        iconActionActive = ColorPalette.Violet20,
        iconActionDisabled = ColorPalette.LightGrey70,
        borderPrimary = ColorPalette.DarkGrey05,
        borderForm = ColorPalette.LightGrey05,
        borderAccent = ColorPalette.Violet30,
        borderWarning = ColorPalette.Red20,
        borderInputEnabled = ColorPalette.DarkGrey05,
        snackbarBkgSuccess = ColorPalette.Violet05,
        snackbarBorderSuccess = ColorPalette.Violet60,
        snackbarTextSuccess = ColorPalette.Violet80,
        snackbarBkgError = ColorPalette.Red05,
        snackbarBorderError = ColorPalette.Red70,
        snackbarTextError = ColorPalette.Red80,
        logoForeground = ColorPalette.Black,
        logoBackground = ColorPalette.White,
        playerControlsForeground = ColorPalette.White,
        playerControlsBackground = ColorPalette.DarkGrey90A80,
    )

private val lightColorPalette =
    FfColors(
        layer1 = ColorPalette.White,
        layer2 = ColorPalette.LightGrey20,
        layerActionPrimaryEnabled = ColorPalette.Violet70,
        layerActionSecondaryEnabled = ColorPalette.White,
        layerActionDisabled = ColorPalette.LightGrey70,
        layerActionWarning = ColorPalette.Yellow40,
        layerAccent = ColorPalette.Violet60,
        scrim = ColorPalette.DarkGrey20,
        actionPrimary = ColorPalette.Violet60,
        actionSecondary = ColorPalette.LightGrey30,
        actionOverlay = ColorPalette.DarkGrey90A80,
        textPrimary = ColorPalette.DarkGrey90,
        textSecondary = ColorPalette.DarkGrey05,
        textLink = ColorPalette.Violet60,
        textWarning = ColorPalette.Red70,
        textActionPrimary = ColorPalette.White,
        textActionSecondary = ColorPalette.DarkGrey90,
        textActionDisabled = ColorPalette.LightGrey90,
        iconPrimary = ColorPalette.DarkGrey90,
        iconSecondary = ColorPalette.DarkGrey05,
        iconAccent = ColorPalette.Violet60,
        iconActionActive = ColorPalette.Violet60,
        iconActionDisabled = ColorPalette.LightGrey70,
        borderPrimary = ColorPalette.LightGrey40,
        borderForm = ColorPalette.DarkGrey90,
        borderAccent = ColorPalette.Violet60,
        borderWarning = ColorPalette.Red70,
        borderInputEnabled = ColorPalette.LightGrey60,
        snackbarBkgSuccess = ColorPalette.Violet05,
        snackbarBorderSuccess = ColorPalette.Violet60,
        snackbarTextSuccess = ColorPalette.Violet80,
        snackbarBkgError = ColorPalette.Red05,
        snackbarBorderError = ColorPalette.Red70,
        snackbarTextError = ColorPalette.Red80,
        logoForeground = ColorPalette.White,
        logoBackground = ColorPalette.Black,
        playerControlsForeground = ColorPalette.White,
        playerControlsBackground = ColorPalette.DarkGrey90A80,
    )

@Suppress("LargeClass", "LongParameterList")
@Stable
data class FfColors(
    // General background colors
    val layer1: Color,
    val layer2: Color,
    val layerAccent: Color,
    val layerActionPrimaryEnabled: Color,
    val layerActionSecondaryEnabled: Color,
    val layerActionDisabled: Color,
    val layerActionWarning: Color,
    val scrim: Color,
    val actionPrimary: Color,
    val actionSecondary: Color,
    val actionOverlay: Color,
    // General text colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textLink: Color,
    val textWarning: Color,
    val textActionPrimary: Color,
    val textActionSecondary: Color,
    val textActionDisabled: Color,
    // General icon colors
    val iconPrimary: Color,
    val iconSecondary: Color,
    val iconAccent: Color,
    val iconActionActive: Color,
    val iconActionDisabled: Color,
    // General border colors
    val borderPrimary: Color,
    val borderForm: Color,
    val borderAccent: Color,
    val borderWarning: Color,
    val borderInputEnabled: Color,
    // Snackbar colors - NOTE these are the same for light and dark mode
    val snackbarBkgSuccess: Color,
    val snackbarBorderSuccess: Color,
    val snackbarTextSuccess: Color,
    val snackbarBkgError: Color,
    val snackbarBorderError: Color,
    val snackbarTextError: Color,
    // logo
    val logoForeground: Color,
    val logoBackground: Color,
    // Media Player
    val playerControlsForeground: Color,
    val playerControlsBackground: Color,
)

@Composable
fun ProvideFfColors(
    colors: FfColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(localFfColors provides colors, content = content)
}

private val localFfColors =
    staticCompositionLocalOf<FfColors> {
        error("No Ff provided")
    }
