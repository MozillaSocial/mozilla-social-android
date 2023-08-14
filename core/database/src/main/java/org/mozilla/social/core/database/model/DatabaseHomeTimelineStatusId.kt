package org.mozilla.social.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseHomeTimelineStatusId(
    @PrimaryKey
    val statusId: String,
)