package org.mozilla.social.core.database

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    @RenameColumn(
        tableName = "statuses",
        fromColumnName = "isFavourited",
        toColumnName = "isFavorited",
    )
    class Schema1to2 : AutoMigrationSpec

    @DeleteColumn(
        tableName = "homeTimeline",
        columnName = "createdAt",
    )
    @DeleteColumn(
        tableName = "localTimeline",
        columnName = "createdAt",
    )
    @DeleteColumn(
        tableName = "federatedTimeline",
        columnName = "createdAt",
    )
    @DeleteColumn(
        tableName = "hashTagTimeline",
        columnName = "createdAt",
    )
    @DeleteColumn(
        tableName = "accountTimeline",
        columnName = "createdAt",
    )
    class Schema7to8 : AutoMigrationSpec

    @RenameColumn(
        tableName = "followings",
        fromColumnName = "followingAccountId",
        toColumnName = "followeeAccountId",
    )
    @DeleteColumn(
        tableName = "followings",
        columnName = "relationshipAccountId",
    )
    @DeleteColumn(
        tableName = "followers",
        columnName = "relationshipAccountId",
    )
    class Schema10to11 : AutoMigrationSpec

    @DeleteTable(
        tableName = "hashTags"
    )
    class Schema14to15 : AutoMigrationSpec

    @DeleteColumn(
        tableName = "mutes",
        columnName = "isMuted",
    )
    @DeleteColumn(
        tableName = "blocks",
        columnName = "isBlocked",
    )
    class Schema17to18 : AutoMigrationSpec

    @DeleteColumn(
        tableName = "accounts",
        columnName = "lastStatusAt",
    )
    class Schema18to19 : AutoMigrationSpec

    class Schema22to23 : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "DELETE FROM notifications"
            )
        }
    }
}
