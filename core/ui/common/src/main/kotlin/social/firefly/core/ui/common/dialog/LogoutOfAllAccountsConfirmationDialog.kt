package social.firefly.core.ui.common.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.text.FfTextButton

@Composable
fun logoutOfAllAccountsConfirmationDialog(
    onConfirmation: () -> Unit
) : DialogOpener {
    var isOpen by remember { mutableStateOf(false) }

    if (isOpen) {
        FfAlertDialog(
            onDismissRequest = { isOpen = false },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.logout_of_all_accounts_confirmation,
                    )
                )
            },
            confirmButton = {
                FfTextButton(
                    onClick = {
                        onConfirmation()
                        isOpen = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.logout_of_account_confirmed))
                }
            },
            dismissButton = {
                FfTextButton(onClick = { isOpen = false  }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    return object : DialogOpener {
        override fun open() {
            isOpen = true
        }
    }
}