/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.social.core.designsystem.theme


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    scrim = FirefoxColor.DarkGrey90,
    actionPrimary = FirefoxColor.Violet60,
    actionSecondary = FirefoxColor.LightGrey30,
    textPrimary = FirefoxColor.LightGrey05,
    textSecondary = FirefoxColor.LightGrey40,
    textLink = FirefoxColor.Violet20,
    textWarning = FirefoxColor.Red20,
    textActionPrimary = FirefoxColor.LightGrey05,
    textActionSecondary = FirefoxColor.DarkGrey90,

    iconPrimary = FirefoxColor.LightGrey05,
    iconSecondary = FirefoxColor.LightGrey40,
    iconAccent = FirefoxColor.Violet30,
    iconActionActive = FirefoxColor.Violet20,
    iconActionDisabled = FirefoxColor.LightGrey70,

    borderPrimary = FirefoxColor.DarkGrey05,
    borderForm = FirefoxColor.LightGrey05,
    borderAccent = FirefoxColor.Violet30,
    borderWarning = FirefoxColor.Red20,
)

private val lightColorPalette = MoSoColors(
    layer1 = FirefoxColor.White,
    layer2 = FirefoxColor.LightGrey20,
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

    iconPrimary = FirefoxColor.DarkGrey90,
    iconSecondary = FirefoxColor.DarkGrey05,
    iconAccent = FirefoxColor.Violet60,
    iconActionActive = FirefoxColor.Violet60,
    iconActionDisabled = FirefoxColor.LightGrey70,

    borderPrimary = FirefoxColor.LightGrey40,
    borderForm = FirefoxColor.DarkGrey90,
    borderAccent = FirefoxColor.Violet60,
    borderWarning = FirefoxColor.Red70,
)


/**
 * A custom Color Palette for Mozilla Social for Android
 */
@Suppress("LargeClass", "LongParameterList")
@Stable
class MoSoColors(
    layer1: Color,
    layer2: Color,
    layerAccent: Color,
    scrim: Color,
    actionPrimary: Color,
    actionSecondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    textLink: Color,
    textWarning: Color,
    textActionPrimary: Color,
    textActionSecondary: Color,
    iconPrimary: Color,
    iconSecondary: Color,
    iconAccent: Color,
    iconActionActive: Color,
    iconActionDisabled: Color,
    borderPrimary: Color,
    borderForm: Color,
    borderAccent: Color,
    borderWarning: Color,
) {
    // Layers

    // Default Screen background, Frontlayer background, App Bar Top, App Bar Bottom, Frontlayer header
    var layer1 by mutableStateOf(layer1)
        private set

    // Card background, Menu background, Dialog, Banner
    var layer2 by mutableStateOf(layer2)
        private set


    // App Bar Top (edit), Text Cursor, Selected Tab Check
    var layerAccent by mutableStateOf(layerAccent)
        private set

    var scrim by mutableStateOf(scrim)
        private set

    // Actions

    // Primary button, Snackbar, Floating action button, Chip selected
    var actionPrimary by mutableStateOf(actionPrimary)
        private set

    // Secondary button
    var actionSecondary by mutableStateOf(actionSecondary)
        private set

    // Text

    // Primary text
    var textPrimary by mutableStateOf(textPrimary)
        private set

    // Secondary text
    var textSecondary by mutableStateOf(textSecondary)
        private set

    var textLink by mutableStateOf(textLink)
        private set

    // Disabled text
    var textDisabled by mutableStateOf(textLink)
        private set

    // Warning text
    var textWarning by mutableStateOf(textWarning)
        private set

    // Action Primary text
    var textActionPrimary by mutableStateOf(textActionPrimary)
        private set

    // Action Secondary text
    var textActionSecondary by mutableStateOf(textActionSecondary)
        private set


    // Icon

    // Primary icon
    var iconPrimary by mutableStateOf(iconPrimary)
        private set


    // Secondary icon
    var iconSecondary by mutableStateOf(iconSecondary)
        private set

    var iconAccentViolet by mutableStateOf(iconAccent)
        private set

    var iconActionActive by mutableStateOf(iconActionActive)
        private set

    var iconActionDisabled by mutableStateOf(iconActionDisabled)
        private set

    // Border

    // Default, Divider, Dotted
    var borderPrimary by mutableStateOf(borderPrimary)
        private set

    // Form parts
    var borderFormDefault by mutableStateOf(borderForm)
        private set

    // Active tab (Nav), Selected tab, Active form
    var borderAccent by mutableStateOf(borderAccent)
        private set

    // Form parts
    var borderWarning by mutableStateOf(borderWarning)
        private set

    fun update(other: MoSoColors) {
        layer1 = other.layer1
        layer2 = other.layer2
        layerAccent = other.layerAccent
        scrim = other.scrim
        actionPrimary = other.actionPrimary
        actionSecondary = other.actionSecondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textDisabled = other.textDisabled
        textWarning = other.textWarning
        textActionPrimary = other.textActionPrimary
        textActionSecondary = other.textActionSecondary
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconAccentViolet = other.iconAccentViolet
        iconActionActive = other.iconActionActive
        iconActionDisabled = other.iconActionDisabled
        borderPrimary = other.borderPrimary
        borderFormDefault = other.borderFormDefault
        borderAccent = other.borderAccent
        borderWarning = other.borderWarning
    }

    /**
     * Return a copy of this [MoSoColors] and optionally overriding any of the provided values.
     */
    internal fun copy(
        layer1: Color = this.layer1,
        layer2: Color = this.layer2,
        layerAccent: Color = this.layerAccent,
        scrim: Color = this.scrim,
        actionPrimary: Color = this.actionPrimary,
        actionSecondary: Color = this.actionSecondary,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        textDisabled: Color = this.textDisabled,
        textWarning: Color = this.textWarning,
        textActionPrimary: Color = this.textActionPrimary,
        textActionSecondary: Color = this.textActionSecondary,
        iconPrimary: Color = this.iconPrimary,
        iconSecondary: Color = this.iconSecondary,
        iconAccentViolet: Color = this.iconAccentViolet,
        iconActionActive: Color = this.iconActionActive,
        iconActionDisabled: Color = this.iconActionDisabled,
        borderPrimary: Color = this.borderPrimary,
        borderFormDefault: Color = this.borderFormDefault,
        borderAccent: Color = this.borderAccent,
        borderWarning: Color = this.borderWarning,
    ): MoSoColors = MoSoColors(
        layer1 = layer1,
        layer2 = layer2,
        layerAccent = layerAccent,
        scrim = scrim,
        actionPrimary = actionPrimary,
        actionSecondary = actionSecondary,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textLink = textDisabled,
        textWarning = textWarning,
        textActionPrimary = textActionPrimary,
        textActionSecondary = textActionSecondary,
        iconPrimary = iconPrimary,
        iconSecondary = iconSecondary,
        iconAccent = iconAccentViolet,
        iconActionActive = iconActionActive,
        iconActionDisabled = iconActionDisabled,
        borderPrimary = borderPrimary,
        borderForm = borderFormDefault,
        borderAccent = borderAccent,
        borderWarning = borderWarning,
    )
}

@Composable
fun ProvideMoSoColors(
    colors: MoSoColors,
    content: @Composable () -> Unit,
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(localMoSoColors provides colorPalette, content = content)
}

private val localMoSoColors = staticCompositionLocalOf<MoSoColors> {
    error("No MoSo provided")
}

@Composable
fun ProvideMoSoTextStyle(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides MoSoTheme.typography.bodyMedium.copy(
            color = MoSoTheme.colors.textPrimary
        ),
        content = content
    )
}
