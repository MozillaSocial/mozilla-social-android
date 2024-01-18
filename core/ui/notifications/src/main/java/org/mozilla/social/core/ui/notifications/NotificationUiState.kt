package org.mozilla.social.core.ui.notifications

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.poll.PollUiState
import org.mozilla.social.core.ui.postcard.PostContentUiState

sealed class NotificationUiState {
    abstract val id: Int
    abstract val timeStamp: StringFactory
    abstract val title: StringFactory
    abstract val avatarUrl: String
    abstract val accountId: String
    abstract val accountName: String

    data class Mention(
        override val id: Int,
        override val timeStamp: StringFactory,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ): NotificationUiState()

    data class NewStatus(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ): NotificationUiState()

    data class Repost(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ): NotificationUiState()

    data class Follow(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
    ): NotificationUiState()

    data class FollowRequest(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
    ): NotificationUiState()

    data class Favorite(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
    ): NotificationUiState()

    data class PollEnded(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ): NotificationUiState()

    data class StatusUpdated(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ): NotificationUiState()
}