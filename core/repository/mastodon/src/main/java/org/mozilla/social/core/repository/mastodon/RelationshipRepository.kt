package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.repository.mastodon.model.account.toDatabaseModel

class RelationshipRepository(
    private val dao: RelationshipsDao,
) {
    fun insertAll(relationships: List<Relationship>) =
        dao.insertAll(relationships.map { it.toDatabaseModel() })

    fun insert(relationship: Relationship) =
        dao.insert(relationship.toDatabaseModel())
}
