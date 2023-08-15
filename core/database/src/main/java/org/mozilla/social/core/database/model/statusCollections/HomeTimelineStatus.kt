package org.mozilla.social.core.database.model.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.Instant
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper

@Entity(tableName = "homeTimeline")
data class HomeTimelineStatus(
    @PrimaryKey
    val statusId: String,
    val createdAt: Instant,
    val accountId: String,
    val boostedStatusId: String? = null,
    val boostedStatusAccountId: String? = null,
)

data class HomeTimelineStatusWrapper(
    @Embedded
    val homeTimelineStatus: HomeTimelineStatus,

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
        parentColumn = "boostedStatusId",
        entityColumn = "statusId"
    )
    val boostedStatus: DatabaseStatus?,

    @Relation(
        parentColumn = "boostedStatusAccountId",
        entityColumn = "accountId",
    )
    val boostedAccount: DatabaseAccount?,
)

fun HomeTimelineStatusWrapper.toStatusWrapper(): StatusWrapper = StatusWrapper(
    status = status,
    account = account,
    boostedStatus = boostedStatus,
    boostedAccount = boostedAccount
)
