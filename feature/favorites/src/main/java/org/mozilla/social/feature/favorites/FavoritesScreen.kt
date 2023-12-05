@file:Suppress("detekt:all")
package org.mozilla.social.feature.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel()
) {
    FavoritesScreen()
}

@Composable
private fun FavoritesScreen() {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            MoSoCloseableTopAppBar(title = stringResource(id = R.string.favorites_title))
        }
    }
}