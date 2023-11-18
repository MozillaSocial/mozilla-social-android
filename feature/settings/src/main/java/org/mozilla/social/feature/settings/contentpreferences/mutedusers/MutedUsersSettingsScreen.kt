package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun MutedUsersSettingsScreen() {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.muted_users_title)) {
        }
    }
}
