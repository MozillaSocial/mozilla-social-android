package org.mozilla.social.core.ui.notifications

import org.mozilla.social.core.model.Notification

fun Notification.toUiState(): NotificationUiState = when (this) {
    is Notification.Mention -> NotificationUiState.Mention(
        id = id,
    )
    is Notification.NewStatus -> NotificationUiState.NewStatus(
        id = id,
    )
    is Notification.Repost -> NotificationUiState.Repost(
        id = id,
    )
    is Notification.Follow -> NotificationUiState.Follow(
        id = id,
    )
    is Notification.FollowRequest -> NotificationUiState.FollowRequest(
        id = id,
    )
    is Notification.Favorite -> NotificationUiState.Favorite(
        id = id,
    )
    is Notification.PollEnded -> NotificationUiState.PollEnded(
        id = id,
    )
    is Notification.StatusUpdated -> NotificationUiState.StatusUpdated(
        id = id,
    )
}