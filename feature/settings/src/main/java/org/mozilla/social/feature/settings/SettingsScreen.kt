package org.mozilla.social.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsSection

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    SettingsScreen(
        onAccountClicked = viewModel::onAccountClicked,
        onPrivacyClicked = viewModel::onPrivacyClicked,
        onAboutClicked = viewModel::onAboutClicked,
    )
}

@Composable
fun SettingsScreen(
    onAccountClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
    onAboutClicked: () -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.settings_title)) {
            SettingsSection(
                title = stringResource(id = R.string.account_settings_title),
                iconPainter = MoSoIcons.identificationCard(),
                onClick = onAccountClicked,
            )
            SettingsSection(
                title = stringResource(id = R.string.privacy_settings_title),
                iconPainter = MoSoIcons.lockKey(),
                onClick = onPrivacyClicked,
            )
            SettingsSection(
                title = stringResource(id = R.string.about_settings_title),
                iconPainter = MoSoIcons.info(),
                onClick = onAboutClicked,
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    KoinApplication(application = {
        modules(previewModule)
    }) {
        MoSoTheme {
            SettingsScreen()
        }
    }
}