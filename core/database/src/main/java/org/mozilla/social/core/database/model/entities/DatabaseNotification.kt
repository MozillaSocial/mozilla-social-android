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
        ForeignKey(
            entity = DatabaseStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["boostedStatusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["statusAccountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["boostedStatusAccountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabasePoll::class,
            parentColumns = ["pollId"],
            childColumns = ["statusPollId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabasePoll::class,
            parentColumns = ["pollId"],
            childColumns = ["boostedPollId"],
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
    // below are field for an optional status
    @ColumnInfo(index = true)
    val statusId: String?,
    @ColumnInfo(index = true)
    val statusAccountId: String?,
    @ColumnInfo(index = true)
    val statusPollId: String?,
    @ColumnInfo(index = true)
    val boostedStatusId: String?,
    @ColumnInfo(index = true)
    val boostedStatusAccountId: String?,
    @ColumnInfo(index = true)
    val boostedPollId: String?,
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
