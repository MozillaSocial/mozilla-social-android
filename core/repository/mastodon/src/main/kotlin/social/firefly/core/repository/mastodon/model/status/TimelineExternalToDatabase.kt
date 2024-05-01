package social.firefly.core.repository.mastodon.model.status

import social.firefly.core.database.model.entities.statusCollections.AccountTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatus
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.model.Status

fun Status.toHomeTimelineStatus(): HomeTimelineStatus =
    HomeTimelineStatus(
        statusId = statusId,
    )

fun Status.toLocalTimelineStatus(): LocalTimelineStatus =
    LocalTimelineStatus(
        statusId = statusId,
    )

fun Status.toFederatedTimelineStatus(): FederatedTimelineStatus =
    FederatedTimelineStatus(
        statusId = statusId,
    )

fun Status.toAccountTimelineStatus(
    timelineType: AccountTimelineType,
): AccountTimelineStatus =
    AccountTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        timelineType = timelineType,
    )

fun Status.toHashTagTimelineStatus(
    hashTag: String,
): HashTagTimelineStatus =
    HashTagTimelineStatus(
        hashTag = hashTag,
        statusId = statusId,
    )
