package org.mozilla.social.core.database.model.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.Instant
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper

@Entity(tableName = "federatedTimeline")
data class FederatedTimelineStatus(
    @PrimaryKey
    val statusId: String,
    val createdAt: Instant,
    val accountId: String,
    val pollId: String?,
    val boostedStatusId: String?,
    val boostedStatusAccountId: String?,
    val boostedPollId: String?,
)

data class FederatedTimelineStatusWrapper(
    @Embedded
    val federatedTimelineStatus: FederatedTimelineStatus,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: DatabaseStatus,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: DatabaseAccount,

    @Relation(
        parentColumn = "pollId",
        entityColumn = "pollId",
    )
    val poll: DatabasePoll?,

    @Relation(
        parentColumn = "boostedStatusId",
        entityColumn = "statusId"
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

fun FederatedTimelineStatusWrapper.toStatusWrapper(): StatusWrapper = StatusWrapper(
    status = status,
    account = account,
    poll = poll,
    boostedStatus = boostedStatus,
    boostedAccount = boostedAccount,
    boostedPoll = boostedPoll,
)
