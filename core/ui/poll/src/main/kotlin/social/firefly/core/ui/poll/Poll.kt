package social.firefly.core.ui.poll

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.button.FfButton

@Composable
fun Poll(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    Column {
        val userVotes = rememberSaveable(pollUiState) { mutableStateOf(pollUiState.usersVotes) }
        pollUiState.pollOptions.forEachIndexed { index, pollOptionUiState ->
            PollOption(
                userVotes = userVotes,
                optionIndex = index,
                pollUiState = pollUiState,
                pollOptionUiState = pollOptionUiState,
                onOptionSelected = { optionIndex ->
                    userVotes.value =
                        if (pollUiState.isMultipleChoice) {
                            if (userVotes.value.contains(optionIndex)) {
                                userVotes.value.toMutableList().apply { remove(optionIndex) }
                            } else {
                                userVotes.value.toMutableList().apply { add(optionIndex) }
                            }
                        } else {
                            if (userVotes.value.contains(optionIndex)) {
                                userVotes.value.toMutableList().apply { remove(optionIndex) }
                            } else {
                                listOf(optionIndex)
                            }
                        }
                },
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
        }
        Text(
            text = pollUiState.pollInfoText.build(LocalContext.current),
            style = FfTheme.typography.bodyMedium,
        )
        if (pollUiState.canVote) {
            FfButton(
                modifier =
                Modifier
                    .fillMaxWidth(),
                onClick = { pollInteractions.onVoteClicked(pollUiState.pollId, userVotes.value) },
                enabled = userVotes.value.isNotEmpty(),
            ) {
                Text(text = stringResource(id = R.string.vote_button))
            }
        }
    }
}

@Composable
private fun PollOption(
    userVotes: MutableState<List<Int>>,
    optionIndex: Int,
    pollUiState: PollUiState,
    pollOptionUiState: PollOptionUiState,
    onOptionSelected: (index: Int) -> Unit,
) {
    val height = 40.dp
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(90.dp))
            .border(
                width = 1.dp,
                color = FfTheme.colors.borderPrimary,
                shape = RoundedCornerShape(90.dp),
            )
            .semantics(mergeDescendants = true) { },
    ) {
        PollOptionFill(
            height = height,
            pollUiState = pollUiState,
            pollOptionUiState = pollOptionUiState,
        )
        PollOptionText(
            height = height,
            userVotes = userVotes,
            optionIndex = optionIndex,
            pollUiState = pollUiState,
            pollOptionUiState = pollOptionUiState,
            onOptionSelected = onOptionSelected,
        )
    }
}

@Composable
private fun PollOptionFill(
    height: Dp,
    pollUiState: PollUiState,
    pollOptionUiState: PollOptionUiState,
) {
    if (pollUiState.showResults) {
        Box(
            modifier =
            Modifier
                .height(height)
                .fillMaxWidth(fraction = pollOptionUiState.fillFraction)
                .clip(RoundedCornerShape(90.dp)),
        ) {
            Box(
                modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun PollOptionText(
    height: Dp,
    userVotes: MutableState<List<Int>>,
    optionIndex: Int,
    pollUiState: PollUiState,
    pollOptionUiState: PollOptionUiState,
    onOptionSelected: (index: Int) -> Unit,
) {
    // split into a separate modifier so clicks are not consumed if the user can't vote.
    val clickModifier = if (pollUiState.canVote) {
        Modifier.clickable {
            onOptionSelected(optionIndex)
        }
    } else Modifier

    NoRipple {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(height)
                .then(clickModifier),
        ) {
            Row(
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
            ) {
                Text(
                    modifier =
                    Modifier
                        .padding(
                            start = 12.dp,
                        )
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    text = pollOptionUiState.title,
                    style = FfTheme.typography.labelMedium,
                )
                if (userVotes.value.contains(optionIndex)) {
                    Icon(
                        modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp),
                        painter = FfIcons.check(),
                        contentDescription = "",
                    )
                }
            }

            if (pollUiState.showResults) {
                Text(
                    modifier =
                    Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    text = pollOptionUiState.voteInfo.build(LocalContext.current),
                    style = FfTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PollPreview() {
    FfTheme {
        FfSurface {
            Poll(
                pollUiState =
                PollUiState(
                    pollOptions =
                    listOf(
                        PollOptionUiState(
                            fillFraction = 0.5f,
                            title = "option 1",
                            voteInfo = StringFactory.literal("50%"),
                        ),
                        PollOptionUiState(
                            fillFraction = 0.25f,
                            title = "option 2 jfkdlsa jfdlsa jfd sjaf io jfkdlsj afod aj fid jifd",
                            voteInfo = StringFactory.literal("25%"),
                        ),
                        PollOptionUiState(
                            fillFraction = 0.25f,
                            title = "option 3 with a really really long title that extends just too far",
                            voteInfo = StringFactory.literal("25%"),
                        ),
                    ),
                    pollInfoText = StringFactory.literal("3 votes - 5 hours left"),
                    isUserCreatedPoll = false,
                    showResults = true,
                    pollId = "",
                    isMultipleChoice = true,
                    usersVotes = listOf(0, 1, 2),
                    isExpired = false,
                    canVote = true,
                ),
                pollInteractions = object : PollInteractions {},
            )
        }
    }
}
