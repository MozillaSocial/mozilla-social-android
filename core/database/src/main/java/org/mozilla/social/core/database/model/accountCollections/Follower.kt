package org.mozilla.social.core.database.model.accountCollections

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseRelationship

/**
 * @param accountId the account ID of the user we are examining
 * @param followerAccountId the account ID of the follower
 * @param position the position in the list, used for sort order
 */
@Entity(
    tableName = "followers",
    primaryKeys = [
        "accountId",
        "followerAccountId",
    ],
    foreignKeys = [
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["followerAccountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DatabaseRelationship::class,
            parentColumns = ["accountId"],
            childColumns = ["followerAccountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class Follower(
    val accountId: String,
    val followerAccountId: String,
    @ColumnInfo(defaultValue = "0")
    val position: Int,
)

data class FollowerWrapper(
    @Embedded
    val follower: Follower,
    @Relation(
        parentColumn = "followerAccountId",
        entityColumn = "accountId",
    )
    val followerAccount: DatabaseAccount,
    @Relation(
        parentColumn = "followerAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)
