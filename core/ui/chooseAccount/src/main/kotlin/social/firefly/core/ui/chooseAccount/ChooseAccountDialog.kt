package social.firefly.core.ui.chooseAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.FfDialog
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
    Column {
        accounts.forEach { account ->
            Account(
                modifier = Modifier.
                    padding(4.dp),
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
    val avatarUrl by uiState.avatarUrl.collectAsStateWithLifecycle(initialValue = "")
    val accountId by uiState.accountId.collectAsStateWithLifecycle(initialValue = "")
    val domain by uiState.domain.collectAsStateWithLifecycle(initialValue = "")
    val userName by uiState.userName.collectAsStateWithLifecycle(initialValue = "")

    Row(
        modifier = modifier
            .clickable {
                chooseAccountInteractions.onAccountClicked(accountId, domain)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(avatarUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${userName}@${domain}",
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
                    accountId = flowOf(),
                    userName = flowOf("john"),
                    domain = flowOf("mozilla.social"),
                    avatarUrl = flowOf()
                ),
                ChooseAccountUiState(
                    accountId = flowOf(),
                    userName = flowOf("john"),
                    domain = flowOf("moz.soc"),
                    avatarUrl = flowOf()
                )
            ),
            chooseAccountInteractions = ChooseAccountInteractionsNoOp
        )
    }
}
