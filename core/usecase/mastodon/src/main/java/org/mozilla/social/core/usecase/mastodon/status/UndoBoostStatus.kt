package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.R

class UndoBoostStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val databaseDelegate: DatabaseDelegate,
    private val statusRepository: StatusRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(boostedStatusId: String) =
        externalScope.async(dispatcherIo) {
            try {
                databaseDelegate.withTransaction {
                    statusRepository.updateBoostCount(boostedStatusId, -1)
                    statusRepository.updateBoosted(boostedStatusId, false)
                }
                val status = statusRepository.unBoostStatus(boostedStatusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                databaseDelegate.withTransaction {
                    statusRepository.updateBoostCount(boostedStatusId, 1)
                    statusRepository.updateBoosted(boostedStatusId, true)
                }
                showSnackbar(
                    text = StringFactory.resource(R.string.error_undoing_repost),
                    isError = true,
                )
                throw UndoBoostStatusFailedException(e)
            }
        }.await()

    class UndoBoostStatusFailedException(e: Exception) : Exception(e)
}
