package social.firefly.core.database.model.entities.notificationCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.database.model.wrappers.NotificationWrapper

@Entity(
    tableName = "mentionNotifications",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseNotification::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class MentionListNotification(
    @PrimaryKey
    val id: Int,
)

data class MentionListNotificationWrapper(
    @Embedded
    val mentionListNotification: MentionListNotification,
    @Relation(
        entity = DatabaseNotification::class,
        parentColumn = "id",
        entityColumn = "id",
    )
    val notificationWrapper: NotificationWrapper
)