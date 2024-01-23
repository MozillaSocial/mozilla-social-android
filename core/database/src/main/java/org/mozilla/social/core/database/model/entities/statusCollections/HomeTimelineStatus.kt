package org.mozilla.social.core.database.model.entities.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper

@Entity(
    tableName = "homeTimeline",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class HomeTimelineStatus(
    @PrimaryKey
    val statusId: String,
)

data class HomeTimelineStatusWrapper(
    @Embedded
    val homeTimelineStatus: HomeTimelineStatus,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper,
)
