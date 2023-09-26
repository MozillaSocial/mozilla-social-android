package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.DatabaseRelationship

@Dao
interface RelationshipsDao : BaseDao<DatabaseRelationship> {

    @Query(
        "SELECT * FROM relationships " +
        "WHERE accountId = :accountId"
    )
    fun getRelationshipFlow(accountId: String): Flow<DatabaseRelationship>
}