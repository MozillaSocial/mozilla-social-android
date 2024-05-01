package social.firefly.core.database.model.entities.hashtagCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseHashTagEntity

@Entity(
    tableName = "followedHashTags",
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
data class FollowedHashTag(
    @PrimaryKey
    val hashTagName: String,
    val position: Int,
)

data class FollowedHashTagWrapper(
    @Embedded
    val followedHashTag: FollowedHashTag,

    @Relation(
        parentColumn = "hashTagName",
        entityColumn = "name",
    )
    val hashTag: DatabaseHashTagEntity,
)