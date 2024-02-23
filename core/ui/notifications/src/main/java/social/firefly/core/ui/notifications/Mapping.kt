package social.firefly.core.ui.notifications

import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.core.model.Notification
import social.firefly.core.ui.postcard.toPostContentUiState
import java.lang.RuntimeException

@Suppress("TooGenericExceptionThrown")
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
        accountName = account.displayName,
    )

    is Notification.NewStatus -> NotificationUiState.NewStatus(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.new_status_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
        postContentUiState = status.toPostContentUiState(
            currentUserAccountId = currentUserAccountId,
        ),
        statusId = status.statusId,
    )

    is Notification.Repost -> NotificationUiState.Repost(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.repost_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
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
        accountName = account.displayName,
    )

    is Notification.FollowRequest -> NotificationUiState.FollowRequest(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.follow_request_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
    )

    is Notification.Favorite -> NotificationUiState.Favorite(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.favorited_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
        postContentUiState = status.toPostContentUiState(
            currentUserAccountId = currentUserAccountId,
            contentWarningOverride = "",
            onlyShowPreviewOfText = true,
        ),
        statusId = status.statusId,
    )

    is Notification.PollEnded -> NotificationUiState.PollEnded(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.poll_ended_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
        postContentUiState = status.toPostContentUiState(
            currentUserAccountId = currentUserAccountId,
        ),
        statusId = status.statusId,
    )

    is Notification.StatusUpdated -> NotificationUiState.StatusUpdated(
        id = id,
        timeStamp = createdAt.timeSinceNow(),
        title = StringFactory.resource(resId = R.string.status_updated_title, account.displayName),
        avatarUrl = account.avatarUrl,
        accountId = account.accountId,
        accountName = account.displayName,
        postContentUiState = status.toPostContentUiState(
            currentUserAccountId = currentUserAccountId,
        ),
        statusId = status.statusId,
    )

    else -> throw RuntimeException("Notification type not implemented")
}