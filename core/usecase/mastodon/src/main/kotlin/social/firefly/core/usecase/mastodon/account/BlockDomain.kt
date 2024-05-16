package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DomainBlocksRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.R

class BlockDomain(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val domainBlocksRepository: DomainBlocksRepository,
    private val relationshipRepository: RelationshipRepository,
    private val timelineRepository: TimelineRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws BlockDomainFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(domain: String) =
        externalScope.async(dispatcherIo) {
            try {
                timelineRepository.removePostsInHomeTimelineForDomain(domain)
                timelineRepository.removePostsInLocalTimelineForDomain(domain)
                timelineRepository.removePostsFromFederatedTimelineForDomain(domain)
                relationshipRepository.updateDomainBlocked(
                    domain = domain,
                    blocked = true,
                )
                domainBlocksRepository.blockDomain(
                    domain = domain,
                )
            } catch (e: Exception) {
                relationshipRepository.updateDomainBlocked(
                    domain = domain,
                    blocked = false,
                )
                showSnackbar(
                    text = StringFactory.resource(R.string.error_blocking_domain),
                    isError = true,
                )
                throw BlockDomainFailedException(e)
            }
        }.await()

    class BlockDomainFailedException(e: Exception) : Exception(e)
}