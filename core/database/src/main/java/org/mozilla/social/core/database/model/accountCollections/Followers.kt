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
data class Followers(
    val accountId: String,
    val followerAccountId: String,
    val usersAccountId: String,
)

data class FollowerWrapper(
    @Embedded
    val followers: Followers,

    @Relation(
        parentColumn = "followerAccountId",
        entityColumn = "accountId",
    )
    val followerAccount: DatabaseAccount,

    @Relation(
        parentColumn = "usersAccountId",
        entityColumn = "accountId",
    )
    val relationship: DatabaseRelationship,
)