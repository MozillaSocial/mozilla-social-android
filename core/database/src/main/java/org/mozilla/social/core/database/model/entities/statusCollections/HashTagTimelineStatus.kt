package org.mozilla.social.core.database.model.entities.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseStatus
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
    ]
)
data class HashTagTimelineStatus(
    val statusId: String,
    val hashTag: String,
)

data class HashTagTimelineStatusWrapper(
    @Embedded
    val hashTagTimelineStatus: HashTagTimelineStatus,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper,
)
