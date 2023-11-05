package org.mozilla.social.feature.settings.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.SettingsViewModel
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun AboutSettingsScreen() {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.about_settings_title)) {
            // TODO@DA
        }
    }
}
