package org.mozilla.social.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.repository.mastodon.model.account.toDatabaseModel
import org.mozilla.social.core.repository.mastodon.model.account.toExternal

class RelationshipRepository(
    private val dao: RelationshipsDao,
) {
    suspend fun insertAll(relationships: List<Relationship>) =
        dao.upsertAll(relationships.map { it.toDatabaseModel() })

    suspend fun insert(relationship: Relationship) =
        dao.upsert(relationship.toDatabaseModel())

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun deleteOldRelationships() = dao.deleteOldRelationships()

    fun getRelationshipFlow(accountId: String): Flow<Relationship> =
        dao.getRelationshipFlow(accountId).map { it.toExternal() }

    suspend fun updateMuted(
        accountId: String,
        isMuted: Boolean,
    ) = dao.updateMuted(
        accountId = accountId,
        isMuted = isMuted,
    )

    suspend fun updateBlocked(
        accountId: String,
        isBlocked: Boolean,
    ) = dao.updateBlocked(
        accountId = accountId,
        isBlocked = isBlocked,
    )

    suspend fun updateFollowing(
        accountId: String,
        isFollowing: Boolean,
    ) = dao.updateFollowing(
        accountId = accountId,
        isFollowing = isFollowing,
    )
}
