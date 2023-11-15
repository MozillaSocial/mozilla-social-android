package org.mozilla.social.core.database.model.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseRelationship

@Entity(
    tableName = "followers",
    primaryKeys = [
        "accountId",
        "followerAccountId",
    ]
)
data class Follower(
    val accountId: String,
    val followerAccountId: String,
    val relationshipAccountId: String,
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
        parentColumn = "relationshipAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)