package org.mozilla.social.core.ui.notifications

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.ui.poll.toPollUiState
import org.mozilla.social.core.ui.postcard.toPostContentUiState

fun Notification.toUiState(
    currentUserAccountId: String,
): NotificationUiState = when (this) {
    is Notification.Mention -> NotificationUiState.Mention(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.mention_title, account.displayName),
        avatarUrl = account.avatarUrl,
        postContentUiState = status.toPostContentUiState(currentUserAccountId),
        accountId = account.accountId,
        statusId = status.statusId,
    )
    is Notification.NewStatus -> NotificationUiState.NewStatus(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.new_status_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
    )
    is Notification.Repost -> NotificationUiState.Repost(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.repost_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        postContentUiState = status.toPostContentUiState(
            currentUserAccountId = currentUserAccountId,
            contentWarningOverride = "",
            onlyShowPreviewOfText = true,
        ),
        statusId = status.statusId,
    )
    is Notification.Follow -> NotificationUiState.Follow(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.follow_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
    )
    is Notification.FollowRequest -> NotificationUiState.FollowRequest(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.follow_request_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
    )
    is Notification.Favorite -> NotificationUiState.Favorite(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.favorited_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
    )
    is Notification.PollEnded -> NotificationUiState.PollEnded(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.poll_ended_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        pollUiState = status.poll?.toPollUiState(
            isUserCreatedPoll = currentUserAccountId == status.account.accountId,
        ),
        statusId = status.statusId,
    )
    is Notification.StatusUpdated -> NotificationUiState.StatusUpdated(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.status_updated_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
    )
}