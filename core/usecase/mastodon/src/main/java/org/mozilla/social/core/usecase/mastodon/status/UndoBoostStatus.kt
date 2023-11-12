package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.storage.mastodon.DatabaseDelegate
import org.mozilla.social.core.storage.mastodon.LocalStatusRepository
import org.mozilla.social.core.usecase.mastodon.R

class UndoBoostStatus(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val localStatusRepository: LocalStatusRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusesToDatabase: SaveStatusesToDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        boostedStatusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            databaseDelegate.withTransaction {
                localStatusRepository.updateBoostCount(boostedStatusId, -1)
                localStatusRepository.updateBoosted(boostedStatusId, false)
            }
            val status = statusRepository.unBoostStatus(boostedStatusId)
            saveStatusesToDatabase(status)
        } catch (e: Exception) {
            databaseDelegate.withTransaction {
                localStatusRepository.updateBoostCount(boostedStatusId, 1)
                localStatusRepository.updateBoosted(boostedStatusId, true)
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