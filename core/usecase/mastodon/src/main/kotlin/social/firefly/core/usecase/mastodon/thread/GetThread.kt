package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.common.Resource
import social.firefly.common.utils.edit
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

    operator fun invoke(
        statusId: String,
        coroutineScope: CoroutineScope,
    ): Flow<Resource<Thread>> =
        flow {
            emit(Resource.Loading())

            try {
                val context = statusRepository.getStatusContext(statusId)
                val allStatuses = buildList {
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

                // must be inside the invoke function because GetThread is a singleton
                val postedStatusesFlow = MutableStateFlow<List<String>>(emptyList())

                coroutineScope.launch {
                    postedStatusesSharedFlow.collect { statusId ->
                        postedStatusesFlow.update {
                            val newList = it.toMutableList().apply {
                                add(statusId)
                            }
                            newList
                        }
                    }
                }

                val flattenedFlow: Flow<List<Status>> = postedStatusesFlow.flatMapLatest {
                    statusRepository.getStatusesFlow(it)
                }

                emitAll(
                    combine(
                        statusRepository.getStatusesFlow(listOf(statusId)),
                        statusRepository.getStatusesFlow(context.ancestors.map { it.statusId }),
                        statusRepository.getStatusesFlow(context.descendants.map { it.statusId }),
                        flattenedFlow,
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
