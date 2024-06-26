package social.firefly.feature.settings.contentpreferences

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.FfSurface
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsSection

@Composable
internal fun ContentPreferencesSettingsScreen(viewModel: ContentPreferencesSettingsViewModel = koinViewModel()) {
    ContentPreferencesSettingsScreen(
        contentPreferencesSettingsInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun ContentPreferencesSettingsScreen(
    contentPreferencesSettingsInteractions: ContentPreferencesSettingsInteractions,
) {
    FfSurface {
        SettingsColumn(
            modifier = Modifier
                .padding(horizontal = FfSpacing.md),
            title = stringResource(id = R.string.content_preferences_title)
        ) {
            SettingsSection(
                title = stringResource(id = R.string.muted_users_title),
                iconPainter = FfIcons.speakerX(),
                onClick = contentPreferencesSettingsInteractions::onMutedUsersClicked,
            )

            SettingsSection(
                title = stringResource(id = R.string.blocked_users_title),
                iconPainter = FfIcons.prohibit(),
                onClick = contentPreferencesSettingsInteractions::onBlockedUsersClicked,
            )

            SettingsSection(
                title = stringResource(id = R.string.blocked_domains_title),
                iconPainter = FfIcons.globeX(),
                onClick = contentPreferencesSettingsInteractions::onBlockedDomainsClicked,
            )
        }
    }
}
