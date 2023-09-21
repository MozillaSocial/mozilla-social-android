package org.mozilla.social.core.designsystem.icon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.component.MoSoSurface

@Composable
fun MosSoBottomSheet(
    visible: Boolean,
    content: @Composable() (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        content = {
            MoSoSurface(modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)) {
                content()
            }
        }
    )
}