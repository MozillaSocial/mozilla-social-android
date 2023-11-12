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

class BoostStatus(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val showSnackbar: ShowSnackbar,
    private val saveStatusesToDatabase: SaveStatusesToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val localStatusRepository: LocalStatusRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        statusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            databaseDelegate.withTransaction {
                localStatusRepository.updateBoostCount(statusId, 1)
                localStatusRepository.updateBoosted(statusId, true)
            }
            val status = statusRepository.boostStatus(statusId)
            saveStatusesToDatabase(status)
        } catch (e: Exception) {
            databaseDelegate.withTransaction {
                localStatusRepository.updateBoostCount(statusId, -1)
                localStatusRepository.updateBoosted(statusId, false)
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_boosting),
                isError = true,
            )
            throw BoostStatusFailedException(e)
        }
    }.await()

    class BoostStatusFailedException(e: Exception) : Exception(e)
}