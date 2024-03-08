package social.firefly.core.database.model.entities.statusCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.wrappers.StatusWrapper


@Entity(
    tableName = "trendingStatuses",
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
data class TrendingStatus(
    @PrimaryKey
    val statusId: String,
    val position: Int,
)

data class TrendingStatusWrapper(
    @Embedded
    val searchedStatus: TrendingStatus,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper,
)
