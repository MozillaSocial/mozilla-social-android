package org.mozilla.social.core.ui.notifications

sealed class NotificationUiState {
    abstract val id: Int

    data class Mention(
        override val id: Int,
    ): NotificationUiState()

    data class NewStatus(
        override val id: Int,
    ): NotificationUiState()

    data class Repost(
        override val id: Int,
    ): NotificationUiState()

    data class Follow(
        override val id: Int,
    ): NotificationUiState()

    data class FollowRequest(
        override val id: Int,
    ): NotificationUiState()

    data class Favorite(
        override val id: Int,
    ): NotificationUiState()

    data class PollEnded(
        override val id: Int,
    ): NotificationUiState()

    data class StatusUpdated(
        override val id: Int,
    ): NotificationUiState()
}