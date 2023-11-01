package org.mozilla.social.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoButtonSecondary
import org.mozilla.social.core.designsystem.component.MoSoSurface

@Composable
internal fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {

    MoSoSurface(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            MoSoButtonSecondary(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { settingsViewModel.onLogoutClicked() }
            ) { Text(text = "logout") }
        }
    }
}