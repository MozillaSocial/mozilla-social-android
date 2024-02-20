package social.firefly.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import social.firefly.common.Version
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsSection

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
    settingsInteractions: social.firefly.feature.settings.SettingsInteractions,
) {
    FfSurface {
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
                    iconPainter = FfIcons.identificationCard(),
                    onClick = settingsInteractions::onAccountClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.content_preferences_title),
                    iconPainter = FfIcons.listChecks(),
                    onClick = settingsInteractions::onContentPreferencesClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.privacy_settings_title),
                    iconPainter = FfIcons.lockKey(),
                    onClick = settingsInteractions::onPrivacyClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.about_settings_title),
                    iconPainter = FfIcons.info(),
                    onClick = settingsInteractions::onAboutClicked,
                )
                SettingsSection(
                    title = stringResource(id = R.string.open_source_licenses),
                    iconPainter = FfIcons.info(),
                    onClick = settingsInteractions::onOpenSourceLicensesClicked,
                )

                var clickCount by remember { mutableIntStateOf(0) }
                if (social.firefly.feature.settings.BuildConfig.DEBUG || clickCount > DEV_UNLOCK_CLICK_COUNT) {
                    SettingsSection(
                        title = stringResource(id = R.string.developer_options_title),
                        iconPainter = FfIcons.robot(),
                        onClick = settingsInteractions::onDeveloperOptionsClicked,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { clickCount++ },
                    text = "v${Version.name}.${Version.code}",
                )
            }
        }
    }
}

private const val DEV_UNLOCK_CLICK_COUNT = 6

@Preview
@Composable
private fun SettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        SettingsScreen(
            settingsInteractions = object : social.firefly.feature.settings.SettingsInteractions {
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