package org.mozilla.social.core.database.model.accountCollections

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseRelationship

/**
 * @param accountId the account ID of the user we are examining
 * @param followeeAccountId the account ID of the followee
 * @param position the position in the list, used for sort order
 */
@Entity(
    tableName = "followings",
    primaryKeys = [
        "accountId",
        "followeeAccountId",
    ]
)
data class Followee(
    val accountId: String,
    val followeeAccountId: String,
    @ColumnInfo(defaultValue = "0")
    val position: Int,
)

data class FolloweeWrapper(
    @Embedded
    val followee: Followee,

    @Relation(
        parentColumn = "followeeAccountId",
        entityColumn = "accountId",
    )
    val followingAccount: DatabaseAccount,

    @Relation(
        parentColumn = "followeeAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)