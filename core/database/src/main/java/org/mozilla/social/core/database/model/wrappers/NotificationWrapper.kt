package org.mozilla.social.core.database.model.wrappers

import androidx.room.Embedded
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.entities.DatabasePoll
import org.mozilla.social.core.database.model.entities.DatabaseStatus
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper

data class NotificationWrapper(
    @Embedded
    val notification: DatabaseNotification,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: DatabaseAccount,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: DatabaseStatus?,
    @Relation(
        parentColumn = "statusAccountId",
        entityColumn = "accountId",
    )
    val statusAccount: DatabaseAccount?,
    @Relation(
        parentColumn = "statusPollId",
        entityColumn = "pollId",
    )
    val poll: DatabasePoll?,
    @Relation(
        parentColumn = "boostedStatusId",
        entityColumn = "statusId",
    )
    val boostedStatus: DatabaseStatus?,
    @Relation(
        parentColumn = "boostedStatusAccountId",
        entityColumn = "accountId",
    )
    val boostedAccount: DatabaseAccount?,
    @Relation(
        parentColumn = "boostedPollId",
        entityColumn = "pollId",
    )
    val boostedPoll: DatabasePoll?,
)

fun NotificationWrapper.extractStatusWrapper(): StatusWrapper? =
    status?.let {
        StatusWrapper(
            status = status,
            account = account,
            poll = poll,
            boostedStatus = boostedStatus,
            boostedAccount = boostedAccount,
            boostedPoll = boostedPoll,
        )
    }