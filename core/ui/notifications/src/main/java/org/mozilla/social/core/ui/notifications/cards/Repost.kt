package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.mozilla.social.core.ui.notifications.NotificationUiState

@Composable
fun RepostNotification(
    uiState: NotificationUiState.Repost
) {
    Text(text = "Repost ${uiState.id}")
}