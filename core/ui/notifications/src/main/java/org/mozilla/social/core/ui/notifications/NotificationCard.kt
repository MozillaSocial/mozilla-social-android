package org.mozilla.social.core.ui.notifications

import androidx.compose.runtime.Composable
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.cards.FavoriteNotification
import org.mozilla.social.core.ui.notifications.cards.FollowNotification
import org.mozilla.social.core.ui.notifications.cards.FollowRequestNotification
import org.mozilla.social.core.ui.notifications.cards.MentionNotification
import org.mozilla.social.core.ui.notifications.cards.NewStatusNotification
import org.mozilla.social.core.ui.notifications.cards.PollEndedNotification
import org.mozilla.social.core.ui.notifications.cards.RepostNotification
import org.mozilla.social.core.ui.notifications.cards.StatusUpdatedNotification
import org.mozilla.social.core.ui.poll.PollInteractions

@Composable
fun NotificationCard(
    uiState: NotificationUiState,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
) {
    when (uiState) {
        is NotificationUiState.Favorite -> FavoriteNotification(uiState = uiState)
        is NotificationUiState.Follow -> FollowNotification(uiState = uiState)
        is NotificationUiState.FollowRequest -> FollowRequestNotification(uiState = uiState)
        is NotificationUiState.Mention -> MentionNotification(
            uiState = uiState,
            htmlContentInteractions = htmlContentInteractions,
            pollInteractions = pollInteractions,
        )
        is NotificationUiState.NewStatus -> NewStatusNotification(uiState = uiState)
        is NotificationUiState.PollEnded -> PollEndedNotification(uiState = uiState)
        is NotificationUiState.Repost -> RepostNotification(uiState = uiState)
        is NotificationUiState.StatusUpdated -> StatusUpdatedNotification(uiState = uiState)
    }
}