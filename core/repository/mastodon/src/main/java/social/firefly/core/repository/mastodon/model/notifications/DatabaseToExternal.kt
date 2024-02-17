package social.firefly.core.repository.mastodon.model.notifications

import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.database.model.wrappers.NotificationWrapper
import social.firefly.core.model.Notification
import social.firefly.core.repository.mastodon.model.status.toExternalModel

@Suppress("ComplexMethod")
fun NotificationWrapper.toExternal(): Notification =
    when(notification.type) {
        DatabaseNotification.Type.MENTION -> status?.let {
            Notification.Mention(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.NEW_STATUS -> status?.let {
            Notification.NewStatus(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.REPOST -> status?.let {
            Notification.Repost(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.FOLLOW -> Notification.Follow(
            id = notification.id,
            createdAt = notification.createdAt,
            account = account.toExternalModel(),
        )
        DatabaseNotification.Type.FOLLOW_REQUEST -> Notification.FollowRequest(
            id = notification.id,
            createdAt = notification.createdAt,
            account = account.toExternalModel(),
        )
        DatabaseNotification.Type.FAVORITE -> status?.let {
            Notification.Favorite(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.POLL_ENDED -> status?.let {
            Notification.PollEnded(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.STATUS_UPDATED -> status?.let {
            Notification.StatusUpdated(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
    }

class MissingDataException: Exception()