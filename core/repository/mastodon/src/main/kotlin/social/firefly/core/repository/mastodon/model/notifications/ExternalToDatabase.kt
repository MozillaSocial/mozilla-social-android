package social.firefly.core.repository.mastodon.model.notifications

import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.model.Notification

fun Notification.toDatabase(): DatabaseNotification? = when (this) {
    is Notification.Mention -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.MENTION,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.NewStatus -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.NEW_STATUS,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.Repost -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.REPOST,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.Favorite -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FAVORITE,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.PollEnded -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.POLL_ENDED,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.StatusUpdated -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.STATUS_UPDATED,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
    )

    is Notification.FollowRequest -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FOLLOW_REQUEST,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = null,
    )

    is Notification.Follow -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FOLLOW,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = null,
    )

    else -> null
}
