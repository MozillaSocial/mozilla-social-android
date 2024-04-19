package social.firefly.core.usecase.mastodon.thread

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import social.firefly.common.tree.MutableTreeNode
import social.firefly.common.tree.TreeNode
import social.firefly.common.tree.toTree
import social.firefly.core.model.Status
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class GetThread internal constructor(
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {
    operator fun invoke(statusId: String): Flow<TreeNode<Status>> =
        flow {
            // emit the original status first since it should be in the database already
            statusRepository.getStatusLocal(statusId)?.let {
                emit(
                    MutableTreeNode(it)
                )
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
                    emit(
                        statuses.toTree(
                            identifier = { it.statusId },
                            parentIdentifier = { it.inReplyToId },
                        )!!
                    )
                }
            )
        }
}
