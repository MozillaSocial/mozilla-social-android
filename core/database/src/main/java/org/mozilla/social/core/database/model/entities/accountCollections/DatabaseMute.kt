package org.mozilla.social.core.database.model.entities.accountCollections

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseRelationship


@Entity(
    tableName = "mutes",
    foreignKeys = [ForeignKey(
        entity = DatabaseAccount::class,
        parentColumns = arrayOf("accountId"),
        childColumns = arrayOf("accountId"),
        onDelete = ForeignKey.CASCADE,
         onUpdate = ForeignKey.CASCADE,
    )]
)
data class DatabaseMute(
    @PrimaryKey val accountId: String,
    val isMuted: Boolean,
    @ColumnInfo(defaultValue = "0")
    val position: Int,
)

data class MuteWrapper(
    @Embedded
    val mute: DatabaseMute,
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