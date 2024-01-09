package org.mozilla.social.feature.settings.developer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.feature.settings.R

@Composable
fun DeveloperOptionsScreen() {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column {
            MoSoCloseableTopAppBar(
                title = stringResource(id = R.string.developer_options_title)
            )
        }
    }
}