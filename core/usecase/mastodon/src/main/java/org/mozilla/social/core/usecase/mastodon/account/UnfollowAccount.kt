package org.mozilla.social.core.usecase.mastodon.account

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.R

class UnfollowAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val timelineRepository: TimelineRepository,
    private val socialDatabase: SocialDatabase,
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
            socialDatabase.withTransaction {
                timelinePosts = timelineRepository.getPostsFromHomeTimelineForAccount(accountId)
                timelineRepository.removePostInHomeTimelineForAccount(accountId)
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, -1)
                socialDatabase.relationshipsDao().updateFollowing(accountId, false)
            }
            val relationship = accountRepository.unfollowAccount(accountId)
            relationshipRepository.insert(relationship)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                timelinePosts?.let { timelineRepository.insertAllIntoHomeTimeline(it) }
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, 1)
                socialDatabase.relationshipsDao().updateFollowing(accountId, true)
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
