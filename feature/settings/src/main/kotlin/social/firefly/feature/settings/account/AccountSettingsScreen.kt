package social.firefly.feature.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import social.firefly.common.Resource
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsSection

@Composable
internal fun AccountSettingsScreen(
    viewModel: AccountSettingsViewModel = koinViewModel()
) {
    val activeAccount: LoggedInAccount by viewModel.activeAccount.collectAsStateWithLifecycle(
        initialValue = LoggedInAccount()
    )

    val otherAccounts: List<LoggedInAccount> by viewModel.otherAccounts.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    AccountSettingsScreen(
        activeAccount = activeAccount,
        otherAccounts = otherAccounts,
        accountSettingsInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun AccountSettingsScreen(
    activeAccount: LoggedInAccount,
    otherAccounts: List<LoggedInAccount>,
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    FfSurface {
        SettingsColumn(
            modifier = Modifier
                .padding(horizontal = FfSpacing.md),
            title = stringResource(id = R.string.account_settings_title)
        ) {
            UserHeader(account = activeAccount)

            Spacer(modifier = Modifier.height(FfSpacing.md))

            ManageAccount(
                account = activeAccount,
                onClick = { accountSettingsInteractions.onManageAccountClicked(activeAccount.domain) }
            )

            SignoutButton(onLogoutClicked = accountSettingsInteractions::onLogoutClicked)

            Spacer(modifier = Modifier.height(FfSpacing.md))

            AddAccount()
        }
    }
}

@Composable
private fun UserHeader(
    account: LoggedInAccount,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(account.avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = account.userName,
            style = FfTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun Avatar(
    avatarUrl: String?,
) {
    AsyncImage(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(FfTheme.colors.layer2),
        model = avatarUrl,
        contentDescription = "",
    )
}

@Composable
private fun ManageAccount(
    account: LoggedInAccount,
    onClick: () -> Unit,
) {
    SettingsSection(
        title = stringResource(id = R.string.manage_account),
        subtitle = stringResource(id = R.string.manage_your_account, account.domain),
        onClick = onClick,
    )
}

@Composable
private fun SignoutButton(
    onLogoutClicked: () -> Unit,
) {
    FfButtonSecondary(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = onLogoutClicked,
    ) { Text(text = stringResource(id = R.string.sign_out)) }
}

@Composable
private fun AddAccount() {
    FfButtonSecondary(onClick = { /*TODO*/ }) {
        Text(text = stringResource(id = R.string.add_account))
    }
}

@Preview
@Composable
private fun AccountSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        AccountSettingsScreen(
            activeAccount = LoggedInAccount(),
            otherAccounts = emptyList(),
            accountSettingsInteractions = AccountSettingsInteractionsNoOp
        )
    }
}
