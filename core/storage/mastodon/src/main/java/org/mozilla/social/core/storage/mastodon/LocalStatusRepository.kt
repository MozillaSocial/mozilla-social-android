package org.mozilla.social.core.storage.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.storage.mastodon.status.toDatabaseModel
import org.mozilla.social.core.storage.mastodon.status.toExternalModel

class LocalStatusRepository(
    private val statusDao: StatusDao,
) {
    suspend fun getStatus(
        statusId: String
    ): Status? {
        val status = statusDao.getStatus(statusId)
        return status?.toExternalModel()
    }

    fun getStatusesFlow(
        statusIds: List<String>,
    ): Flow<List<Status>> = statusDao.getStatuses(statusIds).map {
        it.map { statusWrapper ->
            statusWrapper.toExternalModel()
        }
    }

    suspend fun updateBoostCount(statusId: String, valueChange: Long) {
        statusDao.updateBoostCount(statusId = statusId, valueChange = valueChange)
    }

    suspend fun updateBoosted(statusId: String, isBoosted: Boolean) {
        statusDao.updateBoosted(statusId = statusId, isBoosted = isBoosted)
    }

    fun insertStatuses(statuses: List<Status>) {
        statusDao.insertAll(statuses.map { it.toDatabaseModel() })
    }

    suspend fun deleteStatus(statusId: String) {
        statusDao.deleteStatus(statusId)
    }

    suspend fun updateIsBeingDeleted(statusId: String, isBeingDeleted: Boolean) {
        statusDao.updateIsBeingDeleted(statusId = statusId, isBeingDeleted = isBeingDeleted)
    }

    suspend fun updateFavoriteCount(statusId: String, valueChange: Long) {
        statusDao.updateFavoriteCount(statusId = statusId, valueChange = valueChange)
    }

    suspend fun updateFavorited(statusId: String, isFavorited: Boolean) {
        statusDao.updateFavorited(statusId = statusId, isFavorited = isFavorited)
    }

}