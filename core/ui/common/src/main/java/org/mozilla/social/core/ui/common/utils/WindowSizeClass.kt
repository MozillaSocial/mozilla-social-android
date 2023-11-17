package org.mozilla.social.core.ui.common.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Convenience functions to find the current window class size.
 *
 * https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes#window_size_classes
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun getWindowHeightClass(): WindowHeightSizeClass {
    return (LocalContext.current as? Activity)?.let {
        calculateWindowSizeClass(it).heightSizeClass
    } ?: WindowHeightSizeClass.Compact
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun getWindowWidthClass(): WindowWidthSizeClass {
    return (LocalContext.current as? Activity)?.let {
        calculateWindowSizeClass(it).widthSizeClass
    } ?: WindowWidthSizeClass.Compact
}
