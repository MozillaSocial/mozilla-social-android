package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.NotificationCard
import org.mozilla.social.core.ui.notifications.NotificationInteractionsNoOp
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.poll.Poll
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.poll.PollOptionUiState
import org.mozilla.social.core.ui.poll.PollUiState

@Composable
internal fun PollEndedNotificationContent(
    uiState: NotificationUiState.PollEnded,
    pollInteractions: PollInteractions,
) {
    uiState.pollUiState?.let {
        Poll(
            pollUiState = uiState.pollUiState,
            pollInteractions = pollInteractions,
        )
    }
}

@Preview
@Composable
private fun PollEndedNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.PollEnded(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("A poll ended:"),
                avatarUrl = "",
                pollUiState = PollUiState(
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
                accountId = "",
                statusId = "",
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            pollInteractions = object : PollInteractions {},
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}