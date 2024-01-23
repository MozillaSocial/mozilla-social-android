package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.R

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
