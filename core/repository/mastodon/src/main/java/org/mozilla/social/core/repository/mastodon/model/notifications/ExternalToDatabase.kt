package org.mozilla.social.core.repository.mastodon.model.notifications

import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.model.Notification

fun Notification.toDatabase(): DatabaseNotification = DatabaseNotification(
    id = id,
    type = when (this) {
        is Notification.Mention -> DatabaseNotification.Type.MENTION
        is Notification.NewStatus -> DatabaseNotification.Type.NEW_STATUS
        is Notification.Repost -> DatabaseNotification.Type.REPOST
        is Notification.Favorite -> DatabaseNotification.Type.FAVORITE
        is Notification.PollEnded -> DatabaseNotification.Type.POLL_ENDED
        is Notification.StatusUpdated -> DatabaseNotification.Type.STATUS_UPDATED
        is Notification.FollowRequest -> DatabaseNotification.Type.FOLLOW_REQUEST
        is Notification.Follow -> DatabaseNotification.Type.FOLLOW
    },
    createdAt = createdAt,
    accountId = account.accountId,
    statusId = when (this) {
        is Notification.Mention -> status.statusId
        is Notification.NewStatus -> status.statusId
        is Notification.Repost -> status.statusId
        is Notification.Favorite -> status.statusId
        is Notification.PollEnded -> status.statusId
        is Notification.StatusUpdated -> status.statusId
        else -> null
    },
    statusAccountId = when (this) {
        is Notification.Mention -> status.account.accountId
        is Notification.NewStatus -> status.account.accountId
        is Notification.Repost -> status.account.accountId
        is Notification.Favorite -> status.account.accountId
        is Notification.PollEnded -> status.account.accountId
        is Notification.StatusUpdated -> status.account.accountId
        else -> null
    },
    statusPollId = when (this) {
        is Notification.Mention -> status.poll?.pollId
        is Notification.NewStatus -> status.poll?.pollId
        is Notification.Repost -> status.poll?.pollId
        is Notification.Favorite -> status.poll?.pollId
        is Notification.PollEnded -> status.poll?.pollId
        is Notification.StatusUpdated -> status.poll?.pollId
        else -> null
    },
    boostedStatusId = when (this) {
        is Notification.Mention -> status.boostedStatus?.statusId
        is Notification.NewStatus -> status.boostedStatus?.statusId
        is Notification.Repost -> status.boostedStatus?.statusId
        is Notification.Favorite -> status.boostedStatus?.statusId
        is Notification.PollEnded -> status.boostedStatus?.statusId
        is Notification.StatusUpdated -> status.boostedStatus?.statusId
        else -> null
    },
    boostedStatusAccountId = when (this) {
        is Notification.Mention -> status.boostedStatus?.account?.accountId
        is Notification.NewStatus -> status.boostedStatus?.account?.accountId
        is Notification.Repost -> status.boostedStatus?.account?.accountId
        is Notification.Favorite -> status.boostedStatus?.account?.accountId
        is Notification.PollEnded -> status.boostedStatus?.account?.accountId
        is Notification.StatusUpdated -> status.boostedStatus?.account?.accountId
        else -> null
    },
    boostedPollId = when (this) {
        is Notification.Mention -> status.boostedStatus?.poll?.pollId
        is Notification.NewStatus -> status.boostedStatus?.poll?.pollId
        is Notification.Repost -> status.boostedStatus?.poll?.pollId
        is Notification.Favorite -> status.boostedStatus?.poll?.pollId
        is Notification.PollEnded -> status.boostedStatus?.poll?.pollId
        is Notification.StatusUpdated -> status.boostedStatus?.poll?.pollId
        else -> null
    },
)