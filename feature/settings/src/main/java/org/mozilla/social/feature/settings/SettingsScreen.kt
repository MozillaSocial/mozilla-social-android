package org.mozilla.social.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Version
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsSection

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    SettingsScreen(
        onAccountClicked = viewModel::onAccountClicked,
        onContentPreferencesClicked = viewModel::onContentPreferencesClicked,
        onPrivacyClicked = viewModel::onPrivacyClicked,
        onAboutClicked = viewModel::onAboutClicked,
    )
}

@Composable
fun SettingsScreen(
    onAccountClicked: () -> Unit,
    onContentPreferencesClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
    onAboutClicked: () -> Unit,
) {
    MoSoSurface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            SettingsColumn(
                title = stringResource(id = R.string.settings_title)
            ) {
                SettingsSection(
                    title = stringResource(id = R.string.account_settings_title),
                    iconPainter = MoSoIcons.identificationCard(),
                    onClick = onAccountClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.content_preferences_title),
                    iconPainter = MoSoIcons.listChecks(),
                    onClick = onContentPreferencesClicked,
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

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "v${Version.name}.${Version.code}",
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        SettingsScreen(
            onAboutClicked = {},
            onContentPreferencesClicked = {},
            onPrivacyClicked = {},
            onAccountClicked = {},
        )
    }
}
