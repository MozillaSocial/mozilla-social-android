package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.postcard.PostContent

@Composable
fun MentionNotification(
    uiState: NotificationUiState.Mention,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
) {
    Column {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Mention ${uiState.id}"
        )
        PostContent(
            uiState = uiState.postContentUiState,
            htmlContentInteractions = htmlContentInteractions,
            pollInteractions = pollInteractions,
        )
    }
}