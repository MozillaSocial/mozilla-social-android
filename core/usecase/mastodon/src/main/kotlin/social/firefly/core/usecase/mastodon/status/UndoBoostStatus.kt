package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

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
