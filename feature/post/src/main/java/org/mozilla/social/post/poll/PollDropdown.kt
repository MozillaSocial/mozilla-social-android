package org.mozilla.social.post.poll

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.post.interactions.PollInteractions

@Composable
fun PollDurationDropDown(
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        Modifier
            .clickable {
                expanded.value = true
            },
    ) {
        Column {
            Text(
                text = "Poll Duration",
                fontSize = 12.sp
            )
            Text(
                text = poll.pollDuration.label
            )
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        PollDuration.values().forEach { pollDuration ->
            DropdownMenuItem(
                text = {
                    Text(text = pollDuration.label)
                },
                onClick = {
                    pollInteractions.onPollDurationSelected(pollDuration)
                    expanded.value = false
                }
            )
        }

    }
}

@Preview
@Composable
private fun PollDurationDropDownPreview() {
    MozillaSocialTheme {
        PollDurationDropDown(
            poll = Poll(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false
            ),
            pollInteractions = object : PollInteractions {}
        )
    }
}