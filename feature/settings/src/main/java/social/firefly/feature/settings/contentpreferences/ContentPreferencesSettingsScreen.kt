package social.firefly.feature.settings.contentpreferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.icon.MoSoIcons
import social.firefly.core.ui.common.MoSoSurface
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsSection

@Composable
internal fun ContentPreferencesSettingsScreen(viewModel: ContentPreferencesSettingsViewModel = koinViewModel()) {
    ContentPreferencesSettingsScreen(
        onBlockedUsersClicked = viewModel::onBlockedUsersClicked,
        onMutedUsersClicked = viewModel::onMutedUsersClicked,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun ContentPreferencesSettingsScreen(
    onMutedUsersClicked: () -> Unit,
    onBlockedUsersClicked: () -> Unit,
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
