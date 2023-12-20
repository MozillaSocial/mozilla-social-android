package org.mozilla.social.core.database.model.entities.hashtagCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseHashTagEntity

@Entity(
    tableName = "searchedHashTags",
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
data class SearchedHashTag(
    @PrimaryKey
    val hashTagName: String,
    val position: Int,
)

data class SearchedHashTagWrapper(
    @Embedded
    val searchedHashTag: SearchedHashTag,

    @Relation(
        parentColumn = "hashTagName",
        entityColumn = "name",
    )
    val hashTag: DatabaseHashTagEntity,
)