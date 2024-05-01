package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.Status
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.R

class UnfollowAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val timelineRepository: TimelineRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws UnfollowFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        accountId: String,
        loggedInUserAccountId: String,
    ) = externalScope.async(dispatcherIo) {
        var timelinePosts: List<Status>? = null
        try {
            databaseDelegate.withTransaction {
                timelinePosts = timelineRepository.getPostsFromHomeTimelineForAccount(accountId)
                timelineRepository.removePostInHomeTimelineForAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, -1)
                relationshipRepository.updateFollowing(accountId, false)
            }
            val relationship = accountRepository.unfollowAccount(accountId)
            relationshipRepository.insert(relationship)
        } catch (e: Exception) {
            databaseDelegate.withTransaction {
                timelinePosts?.let { timelineRepository.insertAllIntoHomeTimeline(it) }
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, 1)
                relationshipRepository.updateFollowing(accountId, true)
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_unfollowing_account),
                isError = true,
            )
            throw UnfollowFailedException(e)
        }
    }.await()

    class UnfollowFailedException(e: Exception) : Exception(e)
}
