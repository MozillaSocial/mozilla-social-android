package org.mozilla.social.core.usecase.mastodon.thread

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.model.Status

class GetThreadUseCase(
    private val statusRepository: StatusRepository,
) {

    operator fun invoke(
        statusId: String,
    ): Flow<List<Status>> = flow {
        // emit the original status first since it should be in the database already
        statusRepository.getStatusLocal(statusId)?.let {
            emit(listOf(it))
        }

        val context = statusRepository.getStatusContext(statusId)
        statusRepository.saveStatusesToDatabase(context.ancestors)
        statusRepository.saveStatusesToDatabase(context.descendants)

        val statuses = buildList {
            addAll(context.ancestors.map { it.statusId })
            add(statusId)
            addAll(context.descendants.map { it.statusId })
        }

        emitAll(statusRepository.getStatusesFlow(statuses))
    }
}