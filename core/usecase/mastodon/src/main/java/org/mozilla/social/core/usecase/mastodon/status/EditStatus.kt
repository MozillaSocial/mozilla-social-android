package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.repository.mastodon.StatusRepository

class EditStatus(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {

        }

    class EditStatusFailedException(e: Exception) : Exception(e)
}