package org.mozilla.social.core.domain.status

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.StatusApi

class UndoBoostStatus(
    private val externalScope: CoroutineScope,
    private val statusApi: StatusApi,
    private val socialDatabase: SocialDatabase,
    private val statusRepository: StatusRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        boostedStatusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateBoostCount(boostedStatusId, -1)
                socialDatabase.statusDao().updateBoosted(boostedStatusId, false)
            }
            val status = statusApi.unBoostStatus(boostedStatusId).toExternalModel()
            statusRepository.saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateBoostCount(boostedStatusId, 1)
                socialDatabase.statusDao().updateBoosted(boostedStatusId, true)
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_undoing_boost),
                isError = true,
            )
            throw UndoBoostStatusFailedException(e)
        }
    }.await()

    class UndoBoostStatusFailedException(e: Exception) : Exception(e)
}