package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun BlockedUsersSettingsScreen() {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.blocked_users_title)) {

        }
    }
}