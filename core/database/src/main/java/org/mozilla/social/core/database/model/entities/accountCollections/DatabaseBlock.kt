package org.mozilla.social.core.database.model.entities.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseRelationship

@Entity(
    tableName = "blocks",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = arrayOf("accountId"),
            childColumns = arrayOf("accountId"),
            onDelete = CASCADE,
            onUpdate = CASCADE,
        ),
        ForeignKey(
            entity = DatabaseRelationship::class,
            parentColumns = arrayOf("accountId"),
            childColumns = arrayOf("accountId"),
            onDelete = CASCADE,
            onUpdate = CASCADE,
        )
    ]
)
data class DatabaseBlock(
    @PrimaryKey val accountId: String,
    val position: Int,
)

data class BlockWrapper(
    @Embedded
    val block: DatabaseBlock,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: DatabaseAccount,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val databaseRelationship: DatabaseRelationship,
)