package org.mozilla.social.core.database.model.wrappers

import androidx.room.Embedded
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.entities.DatabaseStatus

data class NotificationWrapper(
    @Embedded
    val notification: DatabaseNotification,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: DatabaseAccount,
    @Relation(
        entity = DatabaseStatus::class,
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val status: StatusWrapper?,
)
