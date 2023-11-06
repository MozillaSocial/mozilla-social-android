package org.mozilla.social.feature.settings.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.compose.koinInject
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun AboutSettingsScreen(
    popBackstack: PopNavBackstack = koinInject(),
) {
    MoSoSurface {
        SettingsColumn(
            title = stringResource(id = R.string.about_settings_title),
            onBackClicked = { popBackstack() },
        ) {

        }
    }
}

data class AboutSettings(
    val title: String,
    val subtitle: String,
)