package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import social.firefly.common.Resource
import social.firefly.core.model.Context
import social.firefly.core.model.Status
import social.firefly.core.model.Thread
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class GetThread internal constructor(
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    private val postedStatusesSharedFlow = MutableSharedFlow<String>()

    /**
     * Used to add new statuses to a thread, like when the user posts a reply in a thread.
     */
    internal suspend fun pushNewStatus(statusId: String) {
        postedStatusesSharedFlow.emit(statusId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        statusId: String,
    ): Flow<Resource<Thread>> =
        flow {
            emit(Resource.Loading())

            try {
                val mainStatus = statusRepository.getStatus(statusId)
                val context = statusRepository.getStatusContext(statusId)
                val allStatuses = buildList {
                    add(mainStatus)
                    addAll(context.ancestors)
                    addAll(context.descendants)
                }
                saveStatusToDatabase(
                    allStatuses.map { status ->
                        status.copy(
                            inReplyToAccountName = allStatuses.find {
                                it.account.accountId == status.inReplyToAccountId
                            }?.account?.displayName
                        )
                    }
                )

                // flow of all statuses that have been created while this GetThread flow exists.
                // Allows us to show new posts from the user in threads
                val postedStatusesFlow: Flow<List<Status>> = postedStatusesSharedFlow
                    .scan(emptyList()) { accumulatedList: List<String>, newStatusId: String ->
                        accumulatedList + newStatusId
                    }.flatMapLatest { statusRepository.getStatusesFlow(it) }

                emitAll(
                    combine(
                        statusRepository.getStatusesFlow(listOf(statusId)),
                        statusRepository.getStatusesFlow(context.ancestors.map { it.statusId }),
                        statusRepository.getStatusesFlow(context.descendants.map { it.statusId }),
                        postedStatusesFlow,
                    ) { rootStatus, ancestors, descendants, postedStatuses ->
                        Resource.Loaded(
                            Thread(
                                status = rootStatus.first(),
                                context = Context(
                                    ancestors = ancestors,
                                    descendants = buildList {
                                        addAll(descendants)
                                        addAll(postedStatuses)
                                    },
                                )
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
}
