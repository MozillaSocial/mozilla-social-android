package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import social.firefly.core.model.Status
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class GetThread internal constructor(
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    operator fun invoke(statusId: String): Flow<List<Status>> =
        flow {
            // emit the original status first since it should be in the database already
            val mainStatus = statusRepository.getStatusLocal(statusId)
            mainStatus?.let {
                emit(listOf(it))
            }

            val context = statusRepository.getStatusContext(statusId)
            val allStatuses = buildList {
                addAll(context.ancestors)
                mainStatus?.let { add(it) }
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

            val statusIds =
                buildList {
                    addAll(context.ancestors.map { it.statusId })
                    add(statusId)
                    addAll(context.descendants.map { it.statusId })
                }

            emitAll(statusRepository.getStatusesFlow(statusIds))
        }
}
