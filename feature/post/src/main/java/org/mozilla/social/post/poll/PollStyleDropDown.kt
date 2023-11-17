package org.mozilla.social.post.poll

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.dropdown.MoSoDropdownMenu
import org.mozilla.social.feature.post.R

@Composable
fun PollStyleDropDown(
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
                text = stringResource(id = R.string.poll_style),
                fontSize = 12.sp,
            )
            Text(
                text = poll.style.label.build(LocalContext.current),
            )
        }
    }

    MoSoDropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        PollStyle.values().forEach { pollStyle ->
            DropdownMenuItem(
                text = {
                    Text(text = pollStyle.label.build(LocalContext.current))
                },
                onClick = {
                    pollInteractions.onPollStyleSelected(pollStyle)
                    expanded.value = false
                },
            )
        }
    }
}

@Preview
@Composable
private fun PollStyleDropDownPreview() {
    MoSoTheme {
        PollStyleDropDown(
            poll =
                Poll(
                    options = listOf("option 1", "option 2"),
                    style = PollStyle.SINGLE_CHOICE,
                    pollDuration = PollDuration.ONE_DAY,
                    hideTotals = false,
                ),
            pollInteractions = object : PollInteractions {},
        )
    }
}
