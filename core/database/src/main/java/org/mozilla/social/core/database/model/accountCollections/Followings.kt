package org.mozilla.social.core.database.model.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseRelationship

@Entity(
    tableName = "followings",
    primaryKeys = [
        "accountId",
        "followingAccountId",
    ]
)
data class Followings(
    val accountId: String,
    val followingAccountId: String,
    val usersAccountId: String,
)

data class FollowingsWrapper(
    @Embedded
    val followings: Followings,

    @Relation(
        parentColumn = "followingAccountId",
        entityColumn = "accountId",
    )
    val followingAccount: DatabaseAccount,

    @Relation(
        parentColumn = "usersAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)