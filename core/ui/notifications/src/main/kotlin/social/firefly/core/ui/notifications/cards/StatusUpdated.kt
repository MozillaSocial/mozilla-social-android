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
import social.firefly.core.ui.postcard.PostContentUiState
import social.firefly.core.ui.postcard.components.PostContent

@Composable
internal fun StatusUpdatedNotificationContent(
    uiState: NotificationUiState.StatusUpdated,
    postCardInteractions: PostCardInteractions,
) {
    PostContent(
        uiState = uiState.postContentUiState,
        postCardInteractions = postCardInteractions,
    )
}

@Preview
@Composable
private fun StatusUpdatedNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.StatusUpdated(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John has updated their post:"),
                avatarUrl = "",
                postContentUiState = PostContentUiState(
                    statusId = "",
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