package org.mozilla.social.core.repository.mastodon.model.status

import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.model.Status

fun Status.toHomeTimelineStatus(): HomeTimelineStatus =
    HomeTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        pollId = poll?.pollId,
        boostedStatusId = boostedStatus?.statusId,
        boostedPollId = boostedStatus?.poll?.pollId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
    )

fun Status.toLocalTimelineStatus(): LocalTimelineStatus =
    LocalTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        pollId = poll?.pollId,
        boostedStatusId = boostedStatus?.statusId,
        boostedPollId = boostedStatus?.poll?.pollId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
    )

fun Status.toFederatedTimelineStatus(): FederatedTimelineStatus =
    FederatedTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        pollId = poll?.pollId,
        boostedStatusId = boostedStatus?.statusId,
        boostedPollId = boostedStatus?.poll?.pollId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
    )

fun Status.toAccountTimelineStatus(): AccountTimelineStatus =
    AccountTimelineStatus(
        statusId = statusId,
        accountId = account.accountId,
        pollId = poll?.pollId,
        boostedStatusId = boostedStatus?.statusId,
        boostedPollId = boostedStatus?.poll?.pollId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
    )