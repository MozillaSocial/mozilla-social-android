package org.mozilla.social.feature.settings.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsGroup
import org.mozilla.social.feature.settings.ui.SettingsSwitch

@Composable
fun PrivacySettingsScreen(
    popBackstack: PopNavBackstack = koinInject(),
    viewModel: PrivacySettingsViewModel = koinViewModel(),
) {
    val isAnalyticsToggled = viewModel.allowAnalytics.collectAsState()

    PrivacySettingsScreen(
        isAnalyticsToggledOn = isAnalyticsToggled.value,
        toggleAnalyticsSwitch = viewModel::toggleAllowAnalytics,
        onBackClicked = { popBackstack() },
    )
}

@Composable
fun PrivacySettingsScreen(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit,
    onBackClicked: () -> Unit,
) {
    MoSoSurface {
        SettingsColumn(
            title = stringResource(id = R.string.privacy_settings_title),
            onBackClicked = onBackClicked,
        ) {
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
        PrivacySettingsScreen(
            isAnalyticsToggledOn = true,
            onBackClicked = {},
            toggleAnalyticsSwitch = {},
        )
    }
}
