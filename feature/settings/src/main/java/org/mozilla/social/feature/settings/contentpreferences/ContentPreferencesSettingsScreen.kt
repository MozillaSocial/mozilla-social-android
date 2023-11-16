package org.mozilla.social.feature.settings.contentpreferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsSection

@Composable
fun ContentPreferencesSettingsScreen(
    viewModel: ContentPreferencesSettingsViewModel = koinViewModel()
) {
    ContentPreferencesSettingsScreen(
        onBlockedUsersClicked = viewModel::onBlockedUsersClicked,
        onMutedUsersClicked = viewModel::onMutedUsersClicked,
    )
}

@Composable
private fun ContentPreferencesSettingsScreen(
    onMutedUsersClicked: () -> Unit,
    onBlockedUsersClicked: () -> Unit
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.content_preferences_title)) {
            SettingsSection(
                title = stringResource(id = R.string.muted_users_title),
                iconPainter = MoSoIcons.speakerX(),
                onClick = onMutedUsersClicked,
            )

            SettingsSection(
                title = stringResource(id = R.string.blocked_users_title),
                iconPainter = MoSoIcons.prohibit(),
                onClick = onBlockedUsersClicked,
            )


        }
    }

}