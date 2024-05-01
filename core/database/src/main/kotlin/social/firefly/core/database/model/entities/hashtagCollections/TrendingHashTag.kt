package social.firefly.core.database.model.entities.hashtagCollections


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseHashTagEntity
import social.firefly.core.model.HashTag

@Entity(
    tableName = "trendingHashTags",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseHashTagEntity::class,
            parentColumns = ["name"],
            childColumns = ["hashTagName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class TrendingHashTag(
    @PrimaryKey
    val hashTagName: String,
    val position: Int,
)

data class TrendingHashTagWrapper(
    @Embedded
    val trendingHashTag: TrendingHashTag,

    @Relation(
        parentColumn = "hashTagName",
        entityColumn = "name",
    )
    val hashTag: DatabaseHashTagEntity,
)
