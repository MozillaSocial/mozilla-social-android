package social.firefly.core.ui.common.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import social.firefly.core.ui.common.text.FfTextButton

@Composable
fun blockAccountConfirmationDialog(
    onConfirmation: () -> Unit
) : DialogOpener {
    var isOpen by remember { mutableStateOf(false) }

    if (isOpen) {
        FfAlertDialog(
            onDismissRequest = { isOpen = false },
            confirmButton = {
                FfTextButton(onClick = { onConfirmation()  }) {
                    Text(text = "Blocky!")
                }
            },
            title = {
                Text(text = "Blocky?")
            },
            dismissButton = {
                FfTextButton(onClick = { isOpen = false  }) {
                    Text(text = "Cancel")
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
