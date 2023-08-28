package org.mozilla.social.core.database

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

object DatabaseMigrations {
    @RenameColumn(
        tableName = "statuses",
        fromColumnName = "isFavourited",
        toColumnName = "isFavorited",
    )
    class Schema1to2 : AutoMigrationSpec
}