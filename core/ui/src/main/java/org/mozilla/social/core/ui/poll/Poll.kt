package org.mozilla.social.core.ui.poll

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.ProvideListItemViewModel

@Composable
fun Poll(
    initialUiState: PollUiState,
) {
    ProvideListItemViewModel {
        val pollViewModel: PollViewModel = koinViewModel(parameters = { parametersOf(initialUiState) })
        MainPoll(
            pollUiState = pollViewModel.pollUiState.collectAsState().value,
            pollInteractions = pollViewModel
        )
    }
}

@Composable
private fun MainPoll(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    Column {
        pollUiState.pollOptions.forEachIndexed { index, pollOptionUiState ->
            PollOption(
                optionIndex = index,
                pollUiState = pollUiState,
                pollOptionUiState = pollOptionUiState,
                pollInteractions = pollInteractions,
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
        }
        Text(text = pollUiState.pollInfoText)
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { pollInteractions.onVoteClicked() },
            enabled = pollUiState.voteButtonEnabled,
        ) {
            Text(text = "Vote")
        }
    }
}

@Composable
private fun PollOption(
    optionIndex: Int,
    pollUiState: PollUiState,
    pollOptionUiState: PollOptionUiState,
    pollInteractions: PollInteractions,
) {
    val height = 40.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(90.dp))
            .border(
                width = 1.dp,
                color = if (pollOptionUiState.selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = RoundedCornerShape(90.dp),
            ),
    ) {
        if (pollUiState.showResults) {
            Box(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth(fraction = pollOptionUiState.fillFraction)
                    .clip(RoundedCornerShape(90.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .fillMaxSize()
                )
            }
        }
        NoRipple {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clickable(
                        enabled = pollOptionUiState.enabled
                    ) {
                        pollInteractions.onOptionClicked(optionIndex,)
                    },
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically),
                    text = pollOptionUiState.title
                )
                if (pollUiState.showResults) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),
                        text = pollOptionUiState.voteInfo
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun PollPreview() {
    MozillaSocialTheme {
//        Poll(
//            isUserCreatedPoll = false,
//            poll = Poll(
//                pollId = "1",
//                isExpired = false,
//                allowsMultipleChoices = false,
//                votesCount = 4,
//                options = listOf(
//                    PollOption(
//                        title = "option 1",
//                        votesCount = 1L
//                    ),
//                    PollOption(
//                        title = "option 2",
//                        votesCount = 3L
//                    )
//                ),
//                emojis = listOf(),
//                hasVoted = true,
//            ),
//            pollInteractions = object : PollInteractions {},
//        )
    }
}