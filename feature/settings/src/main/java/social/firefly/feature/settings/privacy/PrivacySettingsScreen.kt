package social.firefly.feature.settings.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsGroup
import social.firefly.feature.settings.ui.SettingsSwitch

@Composable
internal fun PrivacySettingsScreen(viewModel: PrivacySettingsViewModel = koinViewModel()) {
    val isAnalyticsToggled by viewModel.allowAnalytics.collectAsStateWithLifecycle()

    PrivacySettingsScreen(
        isAnalyticsToggledOn = isAnalyticsToggled,
        toggleAnalyticsSwitch = viewModel::toggleAllowAnalytics,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun PrivacySettingsScreen(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit,
) {
    FfSurface {
        SettingsColumn(title = stringResource(id = R.string.privacy_settings_title)) {
            AllowAnalyticsSwitch(
                initialAllowAnalytics = isAnalyticsToggledOn,
                onAllowAnalyticsSwitchToggled = toggleAnalyticsSwitch,
            )
        }
    }
}

@Composable
private fun AllowAnalyticsSwitch(
    initialAllowAnalytics: Boolean,
    onAllowAnalyticsSwitchToggled: () -> Unit,
) {
    Column {
        SettingsGroup(name = R.string.data_collection_and_use) {
            SettingsSwitch(
                title = R.string.data_collection_title,
                description = R.string.data_collection_description,
                checked = initialAllowAnalytics,
                onCheckedChanged = onAllowAnalyticsSwitchToggled,
            )
        }
    }
}

@Preview
@Composable
private fun PrivacySettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        PrivacySettingsScreen(
            isAnalyticsToggledOn = true,
            toggleAnalyticsSwitch = {},
        )
    }
}
