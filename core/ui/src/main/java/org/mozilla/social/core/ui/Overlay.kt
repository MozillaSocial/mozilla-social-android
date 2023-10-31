package org.mozilla.social.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.mozilla.social.core.designsystem.utils.NoRipple

@Composable
fun TransparentOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000)),
    )
}

@Composable
fun TransparentNoTouchOverlay() {
    NoRipple {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
                .clickable { },
        )
    }
}

@Composable
fun NoTouchOverlay() {
    NoRipple {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { },
        )
    }
}