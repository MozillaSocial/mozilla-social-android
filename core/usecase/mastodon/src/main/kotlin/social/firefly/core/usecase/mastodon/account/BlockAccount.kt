package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.R

class BlockAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val timelineRepository: TimelineRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws BlockFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(accountId: String) =
        externalScope.async(dispatcherIo) {
            try {
                timelineRepository.removePostInHomeTimelineForAccount(accountId)
                timelineRepository.removePostInLocalTimelineForAccount(accountId)
                timelineRepository.removePostsFromFederatedTimelineForAccount(accountId)
                relationshipRepository.updateBlocked(accountId, true)
                val relationship = accountRepository.blockAccount(accountId)
                relationshipRepository.insert(relationship)
            } catch (e: Exception) {
                relationshipRepository.updateBlocked(accountId, false)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_blocking_account),
                    isError = true,
                )
                throw BlockFailedException(e)
            }
        }.await()

    class BlockFailedException(e: Exception) : Exception(e)
}
