package org.mozilla.social.core.repository.mastodon.model.notifications

import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.network.mastodon.model.NetworkNotification
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun NetworkNotification.toExternal(): Notification =
    when (this) {
        is NetworkNotification.Mention -> Notification.Mention(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
        is NetworkNotification.NewStatus -> Notification.NewStatus(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
        is NetworkNotification.Repost -> Notification.Repost(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
        is NetworkNotification.Follow -> Notification.Follow(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )
        is NetworkNotification.FollowRequest -> Notification.FollowRequest(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )
        is NetworkNotification.Favorite -> Notification.Favorite(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
        is NetworkNotification.PollEnded -> Notification.PollEnded(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
        is NetworkNotification.StatusUpdated -> Notification.StatusUpdated(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )
    }