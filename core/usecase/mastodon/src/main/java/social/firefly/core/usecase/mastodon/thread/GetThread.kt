package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import social.firefly.common.Resource
import social.firefly.core.model.Context
import social.firefly.core.model.Thread
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class GetThread internal constructor(
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    operator fun invoke(
        statusId: String,
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

                emitAll(
                    combine(
                        statusRepository.getStatusesFlow(listOf(statusId)),
                        statusRepository.getStatusesFlow(context.ancestors.map { it.statusId }),
                        statusRepository.getStatusesFlow(context.descendants.map { it.statusId }),
                    ) { rootStatus, ancestors, descendants ->
                        Resource.Loaded(
                            Thread(
                                status = rootStatus.first(),
                                context = Context(
                                    ancestors = ancestors,
                                    descendants = descendants,
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
