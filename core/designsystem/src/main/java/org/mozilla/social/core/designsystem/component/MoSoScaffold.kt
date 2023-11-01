package org.mozilla.social.core.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.mozilla.social.core.designsystem.icon.MosSoBottomSheet

@Composable
fun MoSoScaffold(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
    bottomSheetVisible: Boolean,
    bottomSheetContent: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = snackbarHost,
        bottomBar = bottomBar,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            content(it)
            MosSoBottomSheet(visible = bottomSheetVisible, content = bottomSheetContent)
        }
    }
}

