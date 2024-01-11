package org.mozilla.social.core.database.model.entities.notificationCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.wrappers.NotificationWrapper

@Entity(
    tableName = "mainNotifications",
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
data class MainNotification(
    @PrimaryKey
    val id: String,
)

data class MainNotificationWrapper(
    @Embedded
    val mainNotification: MainNotification,
    @Relation(
        entity = DatabaseNotification::class,
        parentColumn = "id",
        entityColumn = "id",
    )
    val notificationWrapper: NotificationWrapper
)
