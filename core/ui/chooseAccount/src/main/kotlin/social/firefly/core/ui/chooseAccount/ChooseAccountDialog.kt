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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.DialogOpener
import social.firefly.core.ui.common.dialog.FfDialog
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
fun chooseAccountDialog(
    accounts: List<ChooseAccountUiState>,
    onAccountClicked: (accountId: String) -> Unit
) : DialogOpener {
    var isOpen by remember { mutableStateOf(false) }

    if (isOpen) {
        FfDialog(
            onDismissRequest = { isOpen = false },
        ) {
            DialogContent(
                accounts = accounts,
                onAccountClicked = onAccountClicked,
            )
        }
    }

    return object : DialogOpener {
        override fun open() {
            isOpen = true
        }
    }
}

@Composable
private fun DialogContent(
    accounts: List<ChooseAccountUiState>,
    onAccountClicked: (accountId: String) -> Unit
) {
    Column {
        accounts.forEach { account ->
            Account(
                modifier = Modifier.
                    padding(4.dp)
                    .clickable {
                        onAccountClicked(account.accountId)
                    },
                uiState = account
            )
        }
    }
}

@Composable
private fun Account(
    modifier: Modifier = Modifier,
    uiState: ChooseAccountUiState,
) {
    Row(
        modifier = modifier,
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
            onAccountClicked = {}
        )
    }
}
