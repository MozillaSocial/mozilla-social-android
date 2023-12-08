package org.mozilla.social.core.database.model.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper

@Entity(
    tableName = "hashTagTimeline",
    primaryKeys = [
        "statusId",
        "hashTag",
    ],
    foreignKeys = [
        ForeignKey(
            entity = DatabaseStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["boostedStatusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["boostedStatusAccountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabasePoll::class,
            parentColumns = ["pollId"],
            childColumns = ["pollId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabasePoll::class,
            parentColumns = ["pollId"],
            childColumns = ["boostedPollId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class HashTagTimelineStatus(
    val statusId: String,
    val hashTag: String,
    val accountId: String,
    val pollId: String?,
    val boostedStatusId: String?,
    val boostedStatusAccountId: String?,
    val boostedPollId: String?,
)

data class HashTagTimelineStatusWrapper(
    @Embedded
    val hashTagTimelineStatus: HashTagTimelineStatus,
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

fun HashTagTimelineStatusWrapper.toStatusWrapper(): StatusWrapper =
    StatusWrapper(
        status = status,
        account = account,
        poll = poll,
        boostedStatus = boostedStatus,
        boostedAccount = boostedAccount,
        boostedPoll = boostedPoll,
    )
