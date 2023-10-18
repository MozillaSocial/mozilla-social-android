@file:Suppress("detekt:all")
package org.mozilla.social.feature.discover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface

@Composable
internal fun DiscoverScreen(
    viewModel: DiscoverViewModel = koinViewModel()
) {
    DiscoverScreen()
}

@Composable
private fun DiscoverScreen() {
    MoSoSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Discover")
    }
}