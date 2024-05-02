package social.firefly.core.database.model.entities.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.wrappers.StatusWrapper

@Entity(
    tableName = "bookmarksTimeline",
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
data class BookmarksTimelineStatus(
    @PrimaryKey
    val statusId: String,
    val position: Int,
)

data class BookmarksTimelineStatusWrapper(
    @Embedded
    val bookmarksTimelineStatus: BookmarksTimelineStatus,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper,
)
