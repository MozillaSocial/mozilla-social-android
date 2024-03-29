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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn
import org.mozilla.social.feature.settings.ui.SettingsSection

@Composable
internal fun AccountSettingsScreen(
    viewModel: AccountSettingsViewModel = koinViewModel()
) {
    val userHeader by viewModel.userHeader.collectAsStateWithLifecycle(
        initialValue = Resource.Loading(),
    )
    val subtitle: StringFactory? by viewModel.subtitle.collectAsStateWithLifecycle(initialValue = null)

    AccountSettingsScreen(
        userHeader = userHeader,
        subtitle = subtitle,
        accountSettingsInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun AccountSettingsScreen(
    userHeader: Resource<UserHeader>,
    subtitle: StringFactory?,
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.account_settings_title)) {
            when (userHeader) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Loaded -> {
                    UserHeader(userHeader = userHeader.data)

                    ManageAccount(subtitle = subtitle, onClick = accountSettingsInteractions::onManageAccountClicked)

                    SignoutButton(onLogoutClicked = accountSettingsInteractions::onLogoutClicked)
                }
            }
        }
    }
}

@Composable
private fun UserHeader(userHeader: UserHeader) {
    Row(
        modifier = Modifier.padding(MoSoSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(userHeader.avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = userHeader.accountName, style = MoSoTheme.typography.labelLarge)
    }
}

@Composable
private fun Avatar(avatarUrl: String) {
    AsyncImage(
        modifier =
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MoSoTheme.colors.layer2),
        model = avatarUrl,
        contentDescription = "",
    )
}

@Composable
private fun ManageAccount(subtitle: StringFactory?, onClick: () -> Unit) {
    SettingsSection(
        title = stringResource(id = R.string.manage_account),
        subtitle = subtitle?.build(LocalContext.current),
        onClick = onClick,
    )
}

@Composable
private fun SignoutButton(onLogoutClicked: () -> Unit) {
    MoSoButtonSecondary(
        modifier =
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onLogoutClicked,
    ) { Text(text = stringResource(id = R.string.sign_out)) }
}

@Preview
@Composable
private fun AccountSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        AccountSettingsScreen(
            userHeader = Resource.Loaded(
                UserHeader(
                    avatarUrl = "",
                    accountName = "account",
                    url = ""
                )
            ),
            subtitle = StringFactory.literal("subtitle"),
            accountSettingsInteractions = object : AccountSettingsInteractions {
                override fun onScreenViewed() = Unit
                override fun onLogoutClicked() = Unit
                override fun onManageAccountClicked() = Unit
            }
        )
    }
}
