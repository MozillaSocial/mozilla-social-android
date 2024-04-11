package social.firefly.core.ui.notifications.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.notifications.NotificationCard
import social.firefly.core.ui.notifications.NotificationInteractionsNoOp
import social.firefly.core.ui.notifications.NotificationUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostContent
import social.firefly.core.ui.postcard.PostContentUiState

@Composable
internal fun NewStatusNotificationContent(
    uiState: NotificationUiState.NewStatus,
    postCardInteractions: PostCardInteractions,
) {
    PostContent(
        uiState = uiState.postContentUiState,
        postCardInteractions = postCardInteractions,
    )
}

@Preview
@Composable
private fun NewStatusNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.NewStatus(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John created a new post:"),
                avatarUrl = "",
                postContentUiState = PostContentUiState(
                    pollUiState = null,
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