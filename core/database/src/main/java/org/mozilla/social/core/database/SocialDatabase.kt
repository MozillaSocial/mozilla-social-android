package org.mozilla.social.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mozilla.social.core.database.dao.HomeTimelineIdsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.model.DatabaseHomeTimelineStatusId
import org.mozilla.social.core.database.model.DatabaseStatus

@Database(
    entities = [
        DatabaseStatus::class,
        DatabaseHomeTimelineStatusId::class,
    ],
    version = 1,
    autoMigrations = [

    ],
    exportSchema = true
)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun statusDao(): StatusDao
    abstract fun homeTimelineIdsDao(): HomeTimelineIdsDao
}