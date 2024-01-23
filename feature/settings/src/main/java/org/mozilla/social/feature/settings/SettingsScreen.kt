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
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        settingsInteractions = viewModel,
    )
}

@Composable
fun SettingsScreen(
    settingsInteractions: SettingsInteractions,
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
                    onClick = settingsInteractions::onAccountClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.content_preferences_title),
                    iconPainter = MoSoIcons.listChecks(),
                    onClick = settingsInteractions::onContentPreferencesClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.privacy_settings_title),
                    iconPainter = MoSoIcons.lockKey(),
                    onClick = settingsInteractions::onPrivacyClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.about_settings_title),
                    iconPainter = MoSoIcons.info(),
                    onClick = settingsInteractions::onAboutClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.open_source_licenses),
                    iconPainter = MoSoIcons.info(),
                    onClick = settingsInteractions::onOpenSourceLicensesClicked,
                )

                if (BuildConfig.DEBUG) {
                    SettingsSection(
                        title = stringResource(id = R.string.developer_options_title),
                        iconPainter = MoSoIcons.robot(),
                        onClick = settingsInteractions::onDeveloperOptionsClicked,
                    )
                }

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
            settingsInteractions = object : SettingsInteractions {
                override fun onScreenViewed() = Unit
                override fun onAboutClicked() = Unit
                override fun onAccountClicked() = Unit
                override fun onContentPreferencesClicked() = Unit
                override fun onPrivacyClicked() = Unit
                override fun onDeveloperOptionsClicked() = Unit
                override fun onOpenSourceLicensesClicked() = Unit
            }
        )
    }
}