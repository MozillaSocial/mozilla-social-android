package org.mozilla.social.core.database

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

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

    @DeleteColumn.Entries(
        DeleteColumn(
            tableName = "notifications",
            columnName = "statusAccountId"
        ),
        DeleteColumn(
            tableName = "notifications",
            columnName = "statusPollId"
        ),
        DeleteColumn(
            tableName = "notifications",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "notifications",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "notifications",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "accountTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "accountTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "accountTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "accountTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "favoritesTimeline",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "favoritesTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "favoritesTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "favoritesTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "favoritesTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "federatedTimeline",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "federatedTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "federatedTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "federatedTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "federatedTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "hashTagTimeline",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "hashTagTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "hashTagTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "hashTagTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "hashTagTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "homeTimeline",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "homeTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "homeTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "homeTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "homeTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "localTimeline",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "localTimeline",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "localTimeline",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "localTimeline",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "localTimeline",
            columnName = "boostedPollId"
        ),
        DeleteColumn(
            tableName = "searchedStatuses",
            columnName = "accountId"
        ),
        DeleteColumn(
            tableName = "searchedStatuses",
            columnName = "pollId"
        ),
        DeleteColumn(
            tableName = "searchedStatuses",
            columnName = "boostedStatusId"
        ),
        DeleteColumn(
            tableName = "searchedStatuses",
            columnName = "boostedStatusAccountId"
        ),
        DeleteColumn(
            tableName = "searchedStatuses",
            columnName = "boostedPollId"
        ),
    )
    class Schema23to24 : AutoMigrationSpec
}
