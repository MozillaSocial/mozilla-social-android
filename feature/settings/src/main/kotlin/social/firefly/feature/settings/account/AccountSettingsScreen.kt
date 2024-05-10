package social.firefly.feature.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfBadge
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.dialog.blockAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.logoutConfirmationDialog
import social.firefly.core.ui.common.dialog.logoutOfAllAccountsConfirmationDialog
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfIconButtonDropDownMenu
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.text.MediumTextTitle
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsSection

@Composable
internal fun AccountSettingsScreen(
    viewModel: AccountSettingsViewModel = koinViewModel()
) {
    val activeAccount: LoggedInAccount? by viewModel.activeAccount.collectAsStateWithLifecycle(
        initialValue = null
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountSettingsScreen(
    activeAccount: LoggedInAccount?,
    otherAccounts: List<LoggedInAccount>,
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    FfSurface {
        Column {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.account_settings_title),
                actions = {
                    AddAccount(
                        accountSettingsInteractions = accountSettingsInteractions,
                    )
                },
                showDivider = true
            )

            Column(
                modifier = Modifier
                    .padding(FfSpacing.md)
                    .verticalScroll(rememberScrollState()),
            ) {

                MediumTextLabel(
                    text = stringResource(id = R.string.accounts_settings_description)
                )

                Spacer(modifier = Modifier.height(FfSpacing.md))

                activeAccount?.let {
                    UserHeader(
                        account = activeAccount,
                        isTheActiveAccount = true,
                        accountSettingsInteractions = accountSettingsInteractions,
                    )
                }

                Spacer(modifier = Modifier.height(FfSpacing.md))

                otherAccounts.forEach {
                    UserHeader(
                        account = it,
                        isTheActiveAccount = false,
                        accountSettingsInteractions = accountSettingsInteractions,
                    )

                    Spacer(modifier = Modifier.height(FfSpacing.md))
                }

                Spacer(modifier = Modifier.height(FfSpacing.md))

                SignOutButton(
                    accountSettingsInteractions = accountSettingsInteractions,
                )

                Spacer(modifier = Modifier.height(FfSpacing.md))
            }
        }
    }
}

@Composable
private fun UserHeader(
    account: LoggedInAccount,
    isTheActiveAccount: Boolean,
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    val avatarUrl by account.avatarUrl.collectAsStateWithLifecycle(initialValue = "")
    val accountId by account.accountId.collectAsStateWithLifecycle(initialValue = "")
    val domain by account.domain.collectAsStateWithLifecycle(initialValue = "")
    val userName by account.userName.collectAsStateWithLifecycle(initialValue = "")

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = "${userName}@${domain}",
            style = FfTheme.typography.labelSmall,
        )
        if (isTheActiveAccount) {
            FfBadge(
                modifier = Modifier.padding(horizontal = FfSpacing.md)
            ) {
                SmallTextLabel(
                    modifier = Modifier.padding(horizontal = FfSpacing.sm),
                    text = stringResource(id = R.string.active_account_label)
                )
            }
        } else {
            FfButtonSecondary(
                modifier = Modifier.padding(start = FfSpacing.md),
                onClick = {
                    accountSettingsInteractions.onSetAccountAsActiveClicked(
                        accountId = accountId,
                        domain = domain,
                    )
                }
            ) {
                MediumTextLabel(text = stringResource(id = R.string.activate_account_button))
            }
        }

        val overflowMenuExpanded = remember { mutableStateOf(false) }

        val logoutDialog = logoutConfirmationDialog(
            accountName = "${userName}@${domain}"
        ) {
            accountSettingsInteractions.onLogoutClicked(
                accountId = accountId,
                domain = domain,
            )
        }

        FfIconButtonDropDownMenu(
            expanded = overflowMenuExpanded,
            dropDownMenuContent = {
                FfDropDownItem(
                    text = stringResource(id = R.string.manage_account_option),
                    expanded = overflowMenuExpanded,
                    onClick = { accountSettingsInteractions.onManageAccountClicked(domain) }
                )
                FfDropDownItem(
                    text = stringResource(id = R.string.remove_account),
                    expanded = overflowMenuExpanded,
                    onClick = { logoutDialog.open() }
                )
            }
        ) {
            Icon(
                painter = FfIcons.moreVertical(),
                contentDescription = stringResource(R.string.overflow_button)
            )
        }
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
private fun SignOutButton(
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    val logoutDialog = logoutOfAllAccountsConfirmationDialog {
        accountSettingsInteractions.onLogoutOfAllAccountsClicked()
    }

    FfButton(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = {
            logoutDialog.open()
        },
    ) { Text(text = stringResource(id = R.string.sign_out)) }
}

@Composable
private fun AddAccount(
    accountSettingsInteractions: AccountSettingsInteractions,
) {
    IconButton(
        onClick = { accountSettingsInteractions.onAddAccountClicked() }
    ) {
        Icon(
            painter = FfIcons.plus(),
            contentDescription = stringResource(id = R.string.add_account)
        )
    }
}

@Preview
@Composable
private fun AccountSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        AccountSettingsScreen(
            activeAccount = LoggedInAccount(
                userName = flowOf("John"),
                domain = flowOf("mozilla.social"),
                avatarUrl = flowOf(),
                accountId = flowOf(),
            ),
            otherAccounts = listOf(
                LoggedInAccount(
                    userName = flowOf("Birdman"),
                    domain = flowOf("mozilla.social"),
                    avatarUrl = flowOf(),
                    accountId = flowOf(),
                )
            ),
            accountSettingsInteractions = AccountSettingsInteractionsNoOp
        )
    }
}
