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
data class Followee(
    val accountId: String,
    val followingAccountId: String,
    val relationshipAccountId: String,
)

data class FolloweeWrapper(
    @Embedded
    val followee: Followee,

    @Relation(
        parentColumn = "followingAccountId",
        entityColumn = "accountId",
    )
    val followingAccount: DatabaseAccount,

    @Relation(
        parentColumn = "relationshipAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)