package org.mozilla.social.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mozilla.social.core.data.repository.model.context.toExternalModel
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.network.StatusApi
import org.mozilla.social.model.Context
import org.mozilla.social.model.Status

class StatusRepository(
    private val statusApi: StatusApi,
    private val socialDatabase: SocialDatabase,
) {

    suspend fun getStatusLocal(
        statusId: String
    ): Status? {
        val status = socialDatabase.statusDao().getStatus(statusId)
        return status?.toExternalModel()
    }

    fun getStatusesFlow(
        statusIds: List<String>,
    ): Flow<List<Status>> = socialDatabase.statusDao().getStatuses(statusIds).map {
        it.map { statusWrapper ->
            statusWrapper.toExternalModel()
        }
    }

    suspend fun saveStatusesToDatabase(statuses: List<Status>) {
        saveStatusToDatabase(*statuses.toTypedArray())
    }

    suspend fun saveStatusToDatabase(vararg statuses: Status) {
        socialDatabase.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }
            socialDatabase.pollDao().insertAll(boostedStatuses.mapNotNull {
                it.poll?.toDatabaseModel()
            })
            socialDatabase.accountsDao().insertAll(boostedStatuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(boostedStatuses.map {
                it.toDatabaseModel()
            })

            socialDatabase.pollDao().insertAll(statuses.mapNotNull {
                it.poll?.toDatabaseModel()
            })
            socialDatabase.accountsDao().insertAll(statuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(statuses.map {
                it.toDatabaseModel()
            })
        }
    }

    suspend fun undoStatusBoost(
        boostedStatusId: String,
    ) {
        socialDatabase.withTransaction {
            socialDatabase.statusDao().updateBoostCount(boostedStatusId, -1)
            socialDatabase.statusDao().updateBoosted(boostedStatusId, false)
        }
        try {
            val status = statusApi.unBoostStatus(boostedStatusId).toExternalModel()
            saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateBoostCount(boostedStatusId, 1)
                socialDatabase.statusDao().updateBoosted(boostedStatusId, true)
            }
            throw e
        }
    }

    suspend fun favoriteStatus(
        statusId: String,
    ) {
        socialDatabase.withTransaction {
            socialDatabase.statusDao().updateFavoriteCount(statusId, 1)
            socialDatabase.statusDao().updateFavorited(statusId, true)
        }
        try {
            val status = statusApi.favoriteStatus(statusId).toExternalModel()
            saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateFavoriteCount(statusId, -1)
                socialDatabase.statusDao().updateFavorited(statusId, false)
            }
            throw e
        }
    }

    suspend fun undoFavoriteStatus(
        statusId: String,
    ) {
        socialDatabase.withTransaction {
            socialDatabase.statusDao().updateFavoriteCount(statusId, -1)
            socialDatabase.statusDao().updateFavorited(statusId, false)
        }
        try {
            val status = statusApi.unFavoriteStatus(statusId).toExternalModel()
            saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateFavoriteCount(statusId, 1)
                socialDatabase.statusDao().updateFavorited(statusId, true)
            }
            throw e
        }
    }

    suspend fun getStatusContext(statusId: String): Context =
        statusApi.getStatusContext(statusId).toExternalModel()

    suspend fun deleteStatus(
        statusId: String,
    ) = withContext(Dispatchers.IO) {
        try {
            socialDatabase.statusDao().updateIsBeingDeleted(statusId, true)
            statusApi.deleteStatus(statusId)
            socialDatabase.withTransaction {
                socialDatabase.homeTimelineDao().deletePost(statusId)
                socialDatabase.localTimelineDao().deletePost(statusId)
                socialDatabase.federatedTimelineDao().deletePost(statusId)
                socialDatabase.hashTagTimelineDao().deletePost(statusId)
                socialDatabase.accountTimelineDao().deletePost(statusId)
                socialDatabase.statusDao().deleteStatus(statusId)
            }
        } catch (e: Exception) {
            socialDatabase.statusDao().updateIsBeingDeleted(statusId, false)
            throw e
        }
    }
}