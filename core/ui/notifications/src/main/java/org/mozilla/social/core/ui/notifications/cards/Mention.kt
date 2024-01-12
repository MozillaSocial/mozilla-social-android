package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.NotificationCard
import org.mozilla.social.core.ui.notifications.NotificationInteractions
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.postcard.PostContent
import org.mozilla.social.core.ui.postcard.PostContentUiState

@Composable
fun MentionNotification(
    uiState: NotificationUiState.Mention,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
    notificationInteractions: NotificationInteractions,
) {
    NotificationCard(
        modifier = Modifier
            .clickable { notificationInteractions.onMentionClicked(uiState.statusId) },
        uiState = uiState,
        notificationInteractions = notificationInteractions,
        content = {
            PostContent(
                uiState = uiState.postContentUiState,
                htmlContentInteractions = htmlContentInteractions,
                pollInteractions = pollInteractions,
            )
        }
    )
}

@Preview
@Composable
private fun MentionNotificationPreview() {
    PreviewTheme {
        MentionNotification(
            uiState = NotificationUiState.Mention(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John mentioned you:"),
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
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            pollInteractions = object : PollInteractions {},
            notificationInteractions = object : NotificationInteractions {
                override fun onAvatarClicked(accountId: String) = Unit
                override fun onMentionClicked(statusId: String) = Unit
            },
        )
    }
}
