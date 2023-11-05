package org.mozilla.social.feature.settings.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.SettingsViewModel
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsGroup
import org.mozilla.social.feature.settings.ui.SettingsSwitch

@Composable
fun PrivacySettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val isAnalyticsToggled = settingsViewModel.isAnalyticsToggledOn.collectAsState()

    PrivacySettingsScreen(
        isAnalyticsToggledOn = isAnalyticsToggled.value,
        toggleAnalyticsSwitch = settingsViewModel::toggleAnalytics,
    )
}

@Composable
fun PrivacySettingsScreen(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.privacy_settings_title)) {
            AllowDataCollectionSwitch(
                isAnalyticsToggledOn = isAnalyticsToggledOn,
                toggleAnalyticsSwitch = toggleAnalyticsSwitch
            )
        }
    }
}

@Composable
private fun AllowDataCollectionSwitch(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit
) {
    Column {
        SettingsGroup(name = R.string.data_collection_and_use) {
            SettingsSwitch(
                title = R.string.data_collection_title,
                description = R.string.data_collection_description,
                checked = isAnalyticsToggledOn,
                onCheckedChanged = toggleAnalyticsSwitch
            )
        }
    }
}


@Preview
@Composable
private fun PrivacySettingsScreenPreview() {
    MoSoTheme {
        PrivacySettingsScreen(isAnalyticsToggledOn = true, {})
    }
}
