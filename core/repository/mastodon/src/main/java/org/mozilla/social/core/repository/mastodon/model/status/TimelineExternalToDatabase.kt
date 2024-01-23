package org.mozilla.social.core.repository.mastodon.model.status

import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.AccountTimelineType

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
