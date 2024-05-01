package social.firefly.post.poll

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dropdown.FfDropDownMenu
import social.firefly.core.ui.common.text.SmallTextLabel

@Composable
fun PollDurationDropDown(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    FfDropDownMenu(
        modifier = modifier,
        expanded = expanded,
        dropDownMenuContent = {
            PollDuration.entries.forEach { pollDuration ->
                DropdownMenuItem(
                    text = {
                        Text(text = pollDuration.label.build(LocalContext.current))
                    },
                    onClick = {
                        pollInteractions.onPollDurationSelected(pollDuration)
                        expanded.value = false
                    },
                )
            }
        }
    ) {
        SmallTextLabel(text = pollUiState.pollDuration.label.build(LocalContext.current))
    }
}

@Preview
@Composable
private fun PollDurationDropDownPreview() {
    FfTheme {
        PollDurationDropDown(
            pollUiState =
            PollUiState(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false,
            ),
            pollInteractions = object : PollInteractions {},
        )
    }
}
