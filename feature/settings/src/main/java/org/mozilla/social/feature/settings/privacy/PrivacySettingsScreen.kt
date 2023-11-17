package org.mozilla.social.feature.settings.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsGroup
import org.mozilla.social.feature.settings.ui.SettingsSwitch

@Composable
fun PrivacySettingsScreen(
    viewModel: PrivacySettingsViewModel = koinViewModel()
) {
    val isAnalyticsToggled by viewModel.allowAnalytics.collectAsStateWithLifecycle()

    PrivacySettingsScreen(
        isAnalyticsToggledOn = isAnalyticsToggled,
        toggleAnalyticsSwitch = viewModel::toggleAllowAnalytics,
    )
}

@Composable
fun PrivacySettingsScreen(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.privacy_settings_title)) {
            AllowAnalyticsSwitch(
                initialAllowAnalytics = isAnalyticsToggledOn,
                onAllowAnalyticsSwitchToggled = toggleAnalyticsSwitch
            )
        }
    }
}

@Composable
private fun AllowAnalyticsSwitch(
    initialAllowAnalytics: Boolean,
    onAllowAnalyticsSwitchToggled: () -> Unit
) {
    Column {
        SettingsGroup(name = R.string.data_collection_and_use) {
            SettingsSwitch(
                title = R.string.data_collection_title,
                description = R.string.data_collection_description,
                checked = initialAllowAnalytics,
                onCheckedChanged = onAllowAnalyticsSwitchToggled
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
