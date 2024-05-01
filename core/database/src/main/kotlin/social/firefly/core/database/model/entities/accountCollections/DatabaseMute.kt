package social.firefly.core.database.model.entities.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseAccount
import social.firefly.core.database.model.entities.DatabaseRelationship


@Entity(
    tableName = "mutes",
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
data class DatabaseMute(
    @PrimaryKey val accountId: String,
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