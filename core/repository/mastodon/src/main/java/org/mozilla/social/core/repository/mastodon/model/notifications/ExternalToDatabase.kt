package org.mozilla.social.core.repository.mastodon.model.notifications

import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.model.Notification

@Suppress("LongMethod")
fun Notification.toDatabase(): DatabaseNotification = when (this) {
    is Notification.Mention -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.MENTION,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.NewStatus -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.NEW_STATUS,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.Repost -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.REPOST,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.Favorite -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FAVORITE,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.PollEnded -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.POLL_ENDED,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.StatusUpdated -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.STATUS_UPDATED,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = status.statusId,
        statusAccountId = status.account.accountId,
        statusPollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
        relationshipAccountId = null,
    )
    is Notification.FollowRequest -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FOLLOW_REQUEST,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = null,
        statusAccountId = null,
        statusPollId = null,
        boostedStatusId = null,
        boostedStatusAccountId = null,
        boostedPollId = null,
        relationshipAccountId = relationship.accountId,
    )
    is Notification.Follow -> DatabaseNotification(
        id = id,
        type = DatabaseNotification.Type.FOLLOW,
        createdAt = createdAt,
        accountId = account.accountId,
        statusId = null,
        statusAccountId = null,
        statusPollId = null,
        boostedStatusId = null,
        boostedStatusAccountId = null,
        boostedPollId = null,
        relationshipAccountId = relationship.accountId,
    )
}
