/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.social.core.designsystem.theme


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
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

/**
 * The theme for Mozilla Social Android.
 */
@Composable
fun MoSoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = when (darkTheme) {
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

    ProvideMoSoColors(colors) {
        ProvideMoSoTextStyle {
            MaterialTheme(
                colorScheme = if (darkTheme) MaterialDarkColorScheme else MaterialLightColorScheme,
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

private val darkColorPalette = MoSoColors(
    layer1 = FirefoxColor.DarkGrey70,
    layer2 = FirefoxColor.DarkGrey40,
    layerAccent = FirefoxColor.Violet60,
    layerActionPrimaryEnabled = FirefoxColor.Violet60,
    layerActionSecondaryEnabled = FirefoxColor.White,
    layerActionDisabled = FirefoxColor.LightGrey70,
    scrim = FirefoxColor.DarkGrey90,
    actionPrimary = FirefoxColor.Violet60,
    actionSecondary = FirefoxColor.LightGrey30,
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
    iconAccentViolet = FirefoxColor.Violet30,

    borderPrimary = FirefoxColor.DarkGrey05,
    borderForm = FirefoxColor.LightGrey05,
    borderAccent = FirefoxColor.Violet30,
    borderWarning = FirefoxColor.Red20,
    borderInputEnabled = FirefoxColor.DarkGrey05,
)

private val lightColorPalette = MoSoColors(
    layer1 = FirefoxColor.White,
    layer2 = FirefoxColor.LightGrey20,
    layerActionPrimaryEnabled = FirefoxColor.Violet70,
    layerActionSecondaryEnabled = FirefoxColor.White,
    layerActionDisabled = FirefoxColor.LightGrey70,
    layerAccent = FirefoxColor.Violet60,
    scrim = FirefoxColor.DarkGrey20,
    actionPrimary = FirefoxColor.Violet60,
    actionSecondary = FirefoxColor.LightGrey30,
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
    iconAccentViolet = FirefoxColor.Violet60,

    borderPrimary = FirefoxColor.LightGrey40,
    borderForm = FirefoxColor.DarkGrey90,
    borderAccent = FirefoxColor.Violet60,
    borderWarning = FirefoxColor.Red70,
    borderInputEnabled = FirefoxColor.LightGrey60,
)


/**
 * A custom Color Palette for Mozilla Social for Android
 */
@Suppress("LargeClass", "LongParameterList")
@Stable
data class MoSoColors(
    val layer1: Color,
    val layer2: Color,
    val layerAccent: Color,
    val layerActionPrimaryEnabled: Color,
    val layerActionSecondaryEnabled: Color,
    val layerActionDisabled: Color,
    val scrim: Color,
    val actionPrimary: Color,
    val actionSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textLink: Color,
    val textWarning: Color,
    val textActionPrimary: Color,
    val textActionSecondary: Color,
    val textActionDisabled: Color,
    val iconPrimary: Color,
    val iconSecondary: Color,
    val iconAccent: Color,
    val iconActionActive: Color,
    val iconActionDisabled: Color,
    val iconAccentViolet: Color,
    val borderPrimary: Color,
    val borderForm: Color,
    val borderAccent: Color,
    val borderWarning: Color,
    val borderInputEnabled: Color,
)

@Composable
fun ProvideMoSoColors(
    colors: MoSoColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(localMoSoColors provides colors, content = content)
}

private val localMoSoColors = staticCompositionLocalOf<MoSoColors> {
    error("No MoSo provided")
}

@Composable
fun ProvideMoSoTextStyle(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides MoSoTheme.colors.textPrimary,
        LocalTextStyle provides MoSoTheme.typography.bodyMedium,
        content = content
    )
}
