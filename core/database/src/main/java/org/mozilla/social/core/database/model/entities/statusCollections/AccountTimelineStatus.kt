package org.mozilla.social.core.database.model.entities.statusCollections

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper
import org.mozilla.social.core.model.AccountTimelineType

@Entity(
    tableName = "accountTimeline",
    primaryKeys = [
        "statusId",
        "accountId",
        "timelineType",
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
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
data class AccountTimelineStatus(
    val statusId: String,
    @ColumnInfo(index = true)
    val accountId: String,
    @ColumnInfo(defaultValue = "POSTS")
    val timelineType: AccountTimelineType,
)

data class AccountTimelineStatusWrapper(
    @Embedded
    val accountTimelineStatus: AccountTimelineStatus,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper,
)
