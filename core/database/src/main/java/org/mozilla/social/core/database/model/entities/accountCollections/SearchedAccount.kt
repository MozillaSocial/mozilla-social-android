package org.mozilla.social.core.database.model.entities.accountCollections

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseRelationship

@Entity(
    tableName = "searchedAccounts",
    foreignKeys = [
        ForeignKey(
            entity = DatabaseAccount::class,
            parentColumns = arrayOf("accountId"),
            childColumns = arrayOf("accountId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class SearchedAccount(
    @PrimaryKey
    val accountId: String,
    val position: Int,
)

data class SearchedAccountWrapper(
    @Embedded
    val searchedAccount: SearchedAccount,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: DatabaseAccount,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val databaseRelationship: DatabaseRelationship,
)