package social.firefly.core.database.model.wrappers

import androidx.room.Embedded
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseAccount
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.database.model.entities.DatabaseStatus

data class StatusWrapper(
    @Embedded
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
