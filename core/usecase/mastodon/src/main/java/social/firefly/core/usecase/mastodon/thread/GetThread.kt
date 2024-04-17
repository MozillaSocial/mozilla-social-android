package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import social.firefly.common.utils.indexOfFirst
import social.firefly.core.model.StatusWithDepth
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class GetThread internal constructor(
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    operator fun invoke(statusId: String): Flow<List<StatusWithDepth>> =
        flow {
            // emit the original status first since it should be in the database already
            statusRepository.getStatusLocal(statusId)?.let {
                emit(listOf(
                    StatusWithDepth(it, 0)
                ))
            }

            val context = statusRepository.getStatusContext(statusId)
            saveStatusToDatabase(context.ancestors)
            saveStatusToDatabase(context.descendants)

            val statusIds =
                buildList {
                    addAll(context.ancestors.map { it.statusId })
                    add(statusId)
                    addAll(context.descendants.map { it.statusId })
                }

            emitAll(
                statusRepository.getStatusesFlow(statusIds).transform { statuses ->
                    val statusIdStack = ArrayDeque<String>()
                    emit(
                        buildList {
                            statuses.forEach { status ->
                                when {
                                    context.ancestors.map { it.statusId }.contains(status.statusId) -> {
                                        add(StatusWithDepth(status, -1))
                                    }
                                    status.statusId == statusId -> {
                                        add(StatusWithDepth(status, 0))
                                        statusIdStack.add(status.statusId)
                                    }
                                    else -> {
                                        val inReplyToId = status.inReplyToId ?: return@forEach
                                        val parentDepth = this.find { it.status.statusId == inReplyToId }?.depth ?: 0

                                        // filter replies beyond the max depth
                                        if (parentDepth == MAX_DEPTH) {
                                            return@forEach
                                        }

                                        val parentIndex = this.indexOfFirst { it.status.statusId == inReplyToId }
                                        val insertIndex = this.indexOfFirst(
                                            startingAtIndex = parentIndex + 1
                                        ) { it.depth <= parentDepth }

                                        if (insertIndex == -1) {
                                            add(StatusWithDepth(status, parentDepth + 1))
                                            return@forEach
                                        }

                                        add(
                                            index = insertIndex,
                                            element = StatusWithDepth(status, parentDepth + 1)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            )
        }

    companion object {
        private const val MAX_DEPTH = 10
    }
}
