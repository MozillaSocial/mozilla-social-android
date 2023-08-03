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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.timeLeft
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.Poll
import org.mozilla.social.model.PollOption

@Composable
fun Poll(
    isUserCreatedPoll: Boolean,
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    Column {
        poll.options.forEach {
            PollOption(
                poll.hasVoted ?: false,
                poll.votesCount,
                it,
                pollInteractions,
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
        }
        Text(text = "${poll.votesCount} votes - ${poll.expiresAt?.timeLeft() ?: "poll closed"}")
    }
}

@Composable
private fun PollOption(
    hasVoted: Boolean,
    votes: Long,
    pollOption: PollOption,
    pollInteractions: PollInteractions,
) {
    val voteFraction = remember(votes, pollOption) {
        if (votes == 0L) return@remember 0f
        pollOption.votesCount?.let {
            it.toFloat() / votes
        } ?: 0f
    }
    val votePercent = remember(voteFraction) {
        (voteFraction * 100).toInt()
    }
    val voteCountText = remember(pollOption.votesCount) {
        val voteText = if (pollOption.votesCount == 1L) {
            "vote"
        } else {
            "votes"
        }
        "${pollOption.votesCount ?: 0} $voteText"
    }
    val height = 40.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(90.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(90.dp),
            ),
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth(fraction = voteFraction)
                .clip(RoundedCornerShape(90.dp)),
        ) {
            Box(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondary)
                .fillMaxSize()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clickable { pollInteractions.onOptionClicked(pollOption) },
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically),
                text = pollOption.title
            )
            if (hasVoted) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    text = "$voteCountText - $votePercent%"
                )
            }
        }
    }

}

@Preview
@Composable
private fun PollPreview() {
    MozillaSocialTheme {
        Poll(
            isUserCreatedPoll = false,
            poll = Poll(
                pollId = "1",
                isExpired = false,
                allowsMultipleChoices = false,
                votesCount = 4,
                options = listOf(
                    PollOption(
                        title = "option 1",
                        votesCount = 1L
                    ),
                    PollOption(
                        title = "option 2",
                        votesCount = 3L
                    )
                ),
                emojis = listOf(),
                hasVoted = true,
            ),
            pollInteractions = object : PollInteractions {},
        )
    }
}