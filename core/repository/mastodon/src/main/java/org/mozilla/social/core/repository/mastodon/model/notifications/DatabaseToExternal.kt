package org.mozilla.social.core.repository.mastodon.model.notifications

import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.wrappers.NotificationWrapper
import org.mozilla.social.core.database.model.wrappers.extractStatusWrapper
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.repository.mastodon.model.account.toExternal
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

@Suppress("ComplexMethod")
fun NotificationWrapper.toExternal(): Notification =
    when(notification.type) {
        DatabaseNotification.Type.MENTION -> extractStatusWrapper()?.let {
            Notification.Mention(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.NEW_STATUS -> extractStatusWrapper()?.let {
            Notification.NewStatus(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.REPOST -> extractStatusWrapper()?.let {
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
            relationship = relationship?.toExternal() ?: throw MissingDataException(),
        )
        DatabaseNotification.Type.FOLLOW_REQUEST -> Notification.FollowRequest(
            id = notification.id,
            createdAt = notification.createdAt,
            account = account.toExternalModel(),
            relationship = relationship?.toExternal()  ?: throw MissingDataException(),
        )
        DatabaseNotification.Type.FAVORITE -> extractStatusWrapper()?.let {
            Notification.Favorite(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.POLL_ENDED -> extractStatusWrapper()?.let {
            Notification.PollEnded(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
        DatabaseNotification.Type.STATUS_UPDATED -> extractStatusWrapper()?.let {
            Notification.StatusUpdated(
                id = notification.id,
                createdAt = notification.createdAt,
                account = account.toExternalModel(),
                status = it.toExternalModel(),
            )
        } ?: throw MissingDataException()
    }

class MissingDataException: Exception()