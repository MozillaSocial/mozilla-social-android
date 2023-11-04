package org.mozilla.social.feature.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.mozilla.social.core.designsystem.component.MoSoButtonSecondary
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.SettingsViewModel
import org.mozilla.social.feature.settings.UserHeader
import org.mozilla.social.feature.settings.previewModule
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsSection

@Composable
fun AccountSettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.account_settings_title)) {
            val userHeader = settingsViewModel.userHeader.collectAsState(initial = null).value

            if (userHeader != null) {
                UserHeader(userHeader = userHeader)
                SettingsSection(
                    title = stringResource(id = R.string.profile_settings_title),
                    subtitle = stringResource(id = R.string.manage_your_account, userHeader.url),
                    onClick = settingsViewModel::onProfileSettingsClicked
                )
                SignoutButton(onLogoutClicked = settingsViewModel::onLogoutClicked)
            }
        }
    }
}

@Composable
private fun UserHeader(userHeader: UserHeader) {
    Row(
        modifier = Modifier.padding(MoSoSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(userHeader.avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = userHeader.accountName, style = MoSoTheme.typography.labelLarge)
    }
}

@Composable
private fun Avatar(avatarUrl: String) {
    AsyncImage(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MoSoTheme.colors.layer2),
        model = avatarUrl,
        contentDescription = "",
    )
}

@Composable
fun SignoutButton(onLogoutClicked: () -> Unit) {
    MoSoButtonSecondary(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onLogoutClicked,
    ) { Text(text = stringResource(id = R.string.sign_out)) }
}

@Preview
@Composable
private fun AccountSettingsScreenPreview() {
    KoinApplication(application = {
        modules(previewModule)
    }) {
        MoSoTheme {
            AccountSettingsScreen()
        }
    }
}