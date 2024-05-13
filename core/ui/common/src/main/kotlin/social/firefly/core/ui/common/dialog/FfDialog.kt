package social.firefly.core.ui.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfCard

@Composable
fun FfDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        FfCard(
            modifier = Modifier
                .background(FfTheme.colors.layer1),
            shape = RoundedCornerShape(16.dp)
        ) {
            content()
        }
    }
}