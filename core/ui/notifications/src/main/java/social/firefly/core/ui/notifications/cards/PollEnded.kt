package social.firefly.core.ui.notifications.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.notifications.NotificationCard
import social.firefly.core.ui.notifications.NotificationInteractionsNoOp
import social.firefly.core.ui.notifications.NotificationUiState
import social.firefly.core.ui.poll.PollOptionUiState
import social.firefly.core.ui.poll.PollUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostContent
import social.firefly.core.ui.postcard.PostContentUiState

@Composable
internal fun PollEndedNotificationContent(
    uiState: NotificationUiState.PollEnded,
    postCardInteractions: PostCardInteractions,
) {
    PostContent(
        uiState = uiState.postContentUiState,
        postCardInteractions = postCardInteractions,
    )
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
                postContentUiState = PostContentUiState(
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
                    statusTextHtml = "this is a status",
                    mediaAttachments = emptyList(),
                    mentions = emptyList(),
                    previewCard = null,
                    contentWarning = "",
                ),
                accountId = "",
                statusId = "",
                accountName = "",
            ),
            postCardInteractions = PostCardInteractionsNoOp,
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}