package social.firefly.core.ui.common.dropdown

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FfDropDownItem(
    text: String,
    expanded: MutableState<Boolean>,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Row {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(text = text)
            }
        },
        onClick = {
            onClick()
            expanded.value = false
        },
    )
}
