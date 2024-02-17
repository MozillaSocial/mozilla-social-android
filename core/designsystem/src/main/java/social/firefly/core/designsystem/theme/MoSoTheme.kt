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
fun MoSoTheme(
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
        ProvideMoSoColors(colors = colors) {
            val textSelectionColors = TextSelectionColors(
                handleColor = MoSoTheme.colors.iconAccent,
                backgroundColor = MoSoTheme.colors.layer2,
            )

            CompositionLocalProvider(
                LocalContentColor provides MoSoTheme.colors.textPrimary,
                LocalTextStyle provides MoSoTheme.typography.bodyMedium,
                LocalTextSelectionColors provides textSelectionColors,
                content = content,
            )
        }
    }
}

object MoSoTheme {
    val colors: MoSoColors
        @Composable
        get() = localMoSoColors.current

    val typography: MoSoTypography
        get() = defaultTypography
}

private val darkColorPalette =
    MoSoColors(
        layer1 = FirefoxColor.DarkGrey70,
        layer2 = FirefoxColor.DarkGrey40,
        layerAccent = FirefoxColor.Violet60,
        layerActionPrimaryEnabled = FirefoxColor.Violet60,
        layerActionSecondaryEnabled = FirefoxColor.White,
        layerActionDisabled = FirefoxColor.LightGrey70,
        layerActionWarning = FirefoxColor.Yellow40,
        scrim = FirefoxColor.DarkGrey90,
        actionPrimary = FirefoxColor.Violet60,
        actionSecondary = FirefoxColor.LightGrey30,
        actionOverlay = FirefoxColor.DarkGrey90,
        textPrimary = FirefoxColor.LightGrey05,
        textSecondary = FirefoxColor.LightGrey40,
        textLink = FirefoxColor.Violet20,
        textWarning = FirefoxColor.Red20,
        textActionPrimary = FirefoxColor.LightGrey05,
        textActionSecondary = FirefoxColor.White,
        textActionDisabled = FirefoxColor.LightGrey90,
        iconPrimary = FirefoxColor.LightGrey05,
        iconSecondary = FirefoxColor.LightGrey40,
        iconAccent = FirefoxColor.Violet30,
        iconActionActive = FirefoxColor.Violet20,
        iconActionDisabled = FirefoxColor.LightGrey70,
        borderPrimary = FirefoxColor.DarkGrey05,
        borderForm = FirefoxColor.LightGrey05,
        borderAccent = FirefoxColor.Violet30,
        borderWarning = FirefoxColor.Red20,
        borderInputEnabled = FirefoxColor.DarkGrey05,
        snackbarBkgSuccess = FirefoxColor.Violet05,
        snackbarBorderSuccess = FirefoxColor.Violet60,
        snackbarTextSuccess = FirefoxColor.Violet80,
        snackbarBkgError = FirefoxColor.Red05,
        snackbarBorderError = FirefoxColor.Red70,
        snackbarTextError = FirefoxColor.Red80,
        logoForeground = FirefoxColor.Black,
        logoBackground = FirefoxColor.White,
    )

private val lightColorPalette =
    MoSoColors(
        layer1 = FirefoxColor.White,
        layer2 = FirefoxColor.LightGrey20,
        layerActionPrimaryEnabled = FirefoxColor.Violet70,
        layerActionSecondaryEnabled = FirefoxColor.White,
        layerActionDisabled = FirefoxColor.LightGrey70,
        layerActionWarning = FirefoxColor.Yellow40,
        layerAccent = FirefoxColor.Violet60,
        scrim = FirefoxColor.DarkGrey20,
        actionPrimary = FirefoxColor.Violet60,
        actionSecondary = FirefoxColor.LightGrey30,
        actionOverlay = FirefoxColor.DarkGrey90A80,
        textPrimary = FirefoxColor.DarkGrey90,
        textSecondary = FirefoxColor.DarkGrey05,
        textLink = FirefoxColor.Violet60,
        textWarning = FirefoxColor.Red70,
        textActionPrimary = FirefoxColor.White,
        textActionSecondary = FirefoxColor.DarkGrey90,
        textActionDisabled = FirefoxColor.LightGrey90,
        iconPrimary = FirefoxColor.DarkGrey90,
        iconSecondary = FirefoxColor.DarkGrey05,
        iconAccent = FirefoxColor.Violet60,
        iconActionActive = FirefoxColor.Violet60,
        iconActionDisabled = FirefoxColor.LightGrey70,
        borderPrimary = FirefoxColor.LightGrey40,
        borderForm = FirefoxColor.DarkGrey90,
        borderAccent = FirefoxColor.Violet60,
        borderWarning = FirefoxColor.Red70,
        borderInputEnabled = FirefoxColor.LightGrey60,
        snackbarBkgSuccess = FirefoxColor.Violet05,
        snackbarBorderSuccess = FirefoxColor.Violet60,
        snackbarTextSuccess = FirefoxColor.Violet80,
        snackbarBkgError = FirefoxColor.Red05,
        snackbarBorderError = FirefoxColor.Red70,
        snackbarTextError = FirefoxColor.Red80,
        logoForeground = FirefoxColor.White,
        logoBackground = FirefoxColor.Black,
    )

@Suppress("LargeClass", "LongParameterList")
@Stable
data class MoSoColors(
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
)

@Composable
fun ProvideMoSoColors(
    colors: MoSoColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(localMoSoColors provides colors, content = content)
}

private val localMoSoColors =
    staticCompositionLocalOf<MoSoColors> {
        error("No MoSo provided")
    }
