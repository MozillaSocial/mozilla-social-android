package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

class DeleteStatus(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            try {
                statusRepository.updateIsBeingDeleted(statusId, true)
                statusRepository.deleteStatus(statusId)
                statusRepository.deleteStatusLocal(statusId)
            } catch (e: Exception) {
                statusRepository.updateIsBeingDeleted(statusId, false)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_deleting_post),
                    isError = true,
                )
                throw DeleteStatusFailedException(e)
            }
        }.await()

    class DeleteStatusFailedException(e: Exception) : Exception(e)
}
