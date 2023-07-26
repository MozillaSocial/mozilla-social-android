package org.mozilla.social.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BoxScope.TransparentOverlay() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(Color(0xAA000000)),
    )
}

@Composable
fun BoxScope.TransparentNoTouchOverlay() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(Color(0xAA000000))
            .clickable { },
    )
}

@Composable
fun BoxScope.NoTouchOverlay() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .clickable { },
    )
}