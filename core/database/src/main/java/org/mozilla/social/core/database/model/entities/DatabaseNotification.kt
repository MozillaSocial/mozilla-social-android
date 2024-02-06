package org.mozilla.social.core.database.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class DatabaseNotification(
    @PrimaryKey
    val id: Int,
    val type: Type,
    val createdAt: Instant,
    @ColumnInfo(index = true)
    val accountId: String,
    @ColumnInfo(index = true)
    val statusId: String?,
) {
    enum class Type {
        MENTION,
        NEW_STATUS,
        REPOST,
        FOLLOW,
        FOLLOW_REQUEST,
        FAVORITE,
        POLL_ENDED,
        STATUS_UPDATED,
    }
}
