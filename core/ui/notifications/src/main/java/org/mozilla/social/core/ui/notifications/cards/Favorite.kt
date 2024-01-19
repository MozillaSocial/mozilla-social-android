package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.NotificationCard
import org.mozilla.social.core.ui.notifications.NotificationInteractionsNoOp
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.postcard.PostContent
import org.mozilla.social.core.ui.postcard.PostContentUiState

@Composable
internal fun FavoriteNotificationContent(
    uiState: NotificationUiState.Favorite,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
) {
    PostContent(
        uiState = uiState.postContentUiState,
        htmlContentInteractions = htmlContentInteractions,
        pollInteractions = pollInteractions,
    )
}

@Preview
@Composable
private fun FavoriteNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.Favorite(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John favorited your post:"),
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
            htmlContentInteractions = object : HtmlContentInteractions {},
            pollInteractions = object : PollInteractions {},
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}
