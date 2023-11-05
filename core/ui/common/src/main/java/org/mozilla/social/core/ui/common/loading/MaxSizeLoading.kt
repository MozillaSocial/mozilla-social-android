package org.mozilla.social.core.ui.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.mozilla.social.core.designsystem.component.MoSoCircularProgressIndicator

@Composable
fun MaxSizeLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        MoSoCircularProgressIndicator()
    }
}