package org.mozilla.social.feature.settings.licenses

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.text.MediumTextBody
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun OpenSourceLicensesScreen() {
    MoSoSurface {
        SettingsColumn(
            title = stringResource(id = R.string.licenses_settings_title),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(MoSoSpacing.lg),
        ) {
            MediumTextBody(text = "Here's where open source licenses will go")
        }
    }
}