package org.mozilla.social.core.ui.notifications

sealed class NotificationUiState {
    abstract val id: String

    data class Mention(
        override val id: String,
    ): NotificationUiState()

    data class NewStatus(
        override val id: String,
    ): NotificationUiState()

    data class Repost(
        override val id: String,
    ): NotificationUiState()

    data class Follow(
        override val id: String,
    ): NotificationUiState()

    data class FollowRequest(
        override val id: String,
    ): NotificationUiState()

    data class Favorite(
        override val id: String,
    ): NotificationUiState()

    data class PollEnded(
        override val id: String,
    ): NotificationUiState()

    data class StatusUpdated(
        override val id: String,
    ): NotificationUiState()
}