package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.mozilla.social.core.ui.notifications.NotificationUiState

@Composable
fun PollEndedNotification(
    uiState: NotificationUiState.PollEnded
) {
    Text(text = "PollEnded ${uiState.id}")
}