package social.firefly.core.repository.mastodon.model.notifications

import social.firefly.core.model.AdminReport
import social.firefly.core.model.Notification
import social.firefly.core.model.RelationshipSeveranceEvent
import social.firefly.core.network.mastodon.model.NetworkAdminReport
import social.firefly.core.network.mastodon.model.NetworkNotification
import social.firefly.core.network.mastodon.model.NetworkRelationshipSeveranceEvent
import social.firefly.core.repository.mastodon.model.status.toExternalModel

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

        is NetworkNotification.AdminSignUp -> Notification.AdminSignUp(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
        )

        is NetworkNotification.AdminReport -> Notification.AdminReport(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            report = report.toExternal(),
        )

        is NetworkNotification.SeveredRelationships -> Notification.SeveredRelationships(
            id = id.toInt(),
            createdAt = createdAt,
            account = account.toExternalModel(),
            severanceEvent = severanceEvent.toExternal(),
        )
    }

fun NetworkAdminReport.toExternal(): AdminReport = AdminReport(
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

fun NetworkRelationshipSeveranceEvent.toExternal(): RelationshipSeveranceEvent = RelationshipSeveranceEvent(
    id = id,
    type = type,
    purged = purged,
    targetName = targetName,
    relationshipsCount = relationshipsCount,
    createdAt = createdAt,
)