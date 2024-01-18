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

class BoostStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            try {
                databaseDelegate.withTransaction {
                    statusRepository.updateBoostCount(statusId, 1)
                    statusRepository.updateBoosted(statusId, true)
                }
                val status = statusRepository.boostStatus(statusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                databaseDelegate.withTransaction {
                    statusRepository.updateBoostCount(statusId, -1)
                    statusRepository.updateBoosted(statusId, false)
                }
                showSnackbar(
                    text = StringFactory.resource(R.string.error_reposting),
                    isError = true,
                )
                throw BoostStatusFailedException(e)
            }
        }.await()

    class BoostStatusFailedException(e: Exception) : Exception(e)
}
