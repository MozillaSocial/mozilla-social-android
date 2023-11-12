package org.mozilla.social.core.storage.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.model.DatabaseRelationship
import org.mozilla.social.core.model.Relationship

class LocalRelationshipRepository(private val relationshipsDao: RelationshipsDao) {
    fun insertRelationship(relationship: Relationship) {
        return relationshipsDao.insert(relationship.toDatabaseModel())
    }

    fun getRelationshipFlow(accountId: String): Flow<Relationship> {
        return relationshipsDao.getRelationshipFlow(accountId).map { it.toExternal() }
    }
}
