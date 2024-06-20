package social.firefly.core.ui.chooseAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.FfDialog
import social.firefly.core.ui.common.text.MediumTextTitle
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
fun ChooseAccountDialog(
    viewModel: ChooseAccountDialogViewModel = koinViewModel()
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val isOpen by viewModel.isOpen.collectAsStateWithLifecycle(initialValue = false)

    if (isOpen) {
        FfDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            onDismissRequest = { viewModel.onDismissRequest() },
        ) {
            DialogContent(
                accounts = accounts,
                chooseAccountInteractions = viewModel,
            )
        }
    }
}

@Composable
private fun DialogContent(
    accounts: List<ChooseAccountUiState>,
    chooseAccountInteractions: ChooseAccountInteractions,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        MediumTextTitle(text = stringResource(id = R.string.choose_account_dialog_title))
        Spacer(modifier = Modifier.height(8.dp))
        accounts.forEach { account ->
            Account(
                uiState = account,
                chooseAccountInteractions = chooseAccountInteractions,
            )
        }
    }
}

@Composable
private fun Account(
    modifier: Modifier = Modifier,
    uiState: ChooseAccountUiState,
    chooseAccountInteractions: ChooseAccountInteractions,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                chooseAccountInteractions.onAccountClicked(uiState.accountId, uiState.domain)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(uiState.avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${uiState.userName}@${uiState.domain}",
            style = FfTheme.typography.labelSmall,
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

@Preview
@Composable
private fun ChooseAccountDialogPreview() {
    PreviewTheme {
        DialogContent(
            accounts = listOf(
                ChooseAccountUiState(
                    accountId = "",
                    userName = "john",
                    domain = "mozilla.social",
                    avatarUrl = ""
                ),
                ChooseAccountUiState(
                    accountId = "",
                    userName = "john",
                    domain = "moz.soc",
                    avatarUrl = ""
                )
            ),
            chooseAccountInteractions = ChooseAccountInteractionsNoOp
        )
    }
}
