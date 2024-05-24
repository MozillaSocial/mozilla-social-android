package social.firefly.core.repository.mastodon.model.notifications

import social.firefly.core.model.AdminReport
import social.firefly.core.model.Notification
import social.firefly.core.model.RelationshipSeveranceEvent
import social.firefly.core.network.mastodon.model.responseBody.NetworkAdminReport
import social.firefly.core.network.mastodon.model.responseBody.NetworkNotification
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationshipSeveranceEvent
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.toExternal(): Notification =
    when (this) {
        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.Mention -> Notification.Mention(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.NewStatus -> Notification.NewStatus(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.Repost -> Notification.Repost(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.Follow -> Notification.Follow(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.FollowRequest -> Notification.FollowRequest(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.Favorite -> Notification.Favorite(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.PollEnded -> Notification.PollEnded(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.StatusUpdated -> Notification.StatusUpdated(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            status = status.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.AdminSignUp -> Notification.AdminSignUp(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.AdminReport -> Notification.AdminReport(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            report = report.toExternal(),
        )

        is social.firefly.core.network.mastodon.model.responseBody.NetworkNotification.SeveredRelationships -> Notification.SeveredRelationships(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            severanceEvent = severanceEvent.toExternal(),
        )
    }

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAdminReport.toExternal(): AdminReport = AdminReport(
    id = id,
    actionTaken = actionTaken,
    actionTakenAt = actionTakenAt,
    category = category,
    comment = comment,
    forwarded = forwarded,
    createdAt = createdAt,
    statusIds = statusIds,
    ruleIds = ruleIds,
    targetAccount = targetAccount.toExternalModel(),
)

fun social.firefly.core.network.mastodon.model.responseBody.NetworkRelationshipSeveranceEvent.toExternal(): RelationshipSeveranceEvent = RelationshipSeveranceEvent(
    id = id,
    type = type,
    purged = purged,
    targetName = targetName,
    relationshipsCount = relationshipsCount,
    createdAt = createdAt,
)