package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.Avatar
import org.mozilla.social.core.ui.notifications.NotificationMetaData
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.postcard.PostContent
import org.mozilla.social.core.ui.postcard.PostContentUiState

@Composable
fun MentionNotification(
    uiState: NotificationUiState.Mention,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
) {
    Row {
        Avatar(avatarUrl = uiState.avatarUrl)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            NotificationMetaData(
                modifier = Modifier
                    .padding(bottom = 9.dp, top = 9.dp),
                uiState = uiState,
            )
            PostContent(
                uiState = uiState.postContentUiState,
                htmlContentInteractions = htmlContentInteractions,
                pollInteractions = pollInteractions,
            )
        }
    }
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
                    contentWarning = ""
                )
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            pollInteractions = object : PollInteractions {},
        )
    }
}
