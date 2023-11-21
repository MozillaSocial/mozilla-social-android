package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.R

class FollowAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws FollowFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        accountId: String,
        loggedInUserAccountId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            databaseDelegate.withTransaction {
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, 1)
                relationshipRepository.updateFollowing(accountId, true)
            }
            val relationship = accountRepository.followAccount(accountId)
            relationshipRepository.insert(relationship)
        } catch (e: Exception) {
            databaseDelegate.withTransaction {
                accountRepository.updateFollowingCountInDatabase(loggedInUserAccountId, -1)
                relationshipRepository.updateFollowing(accountId, false)
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_following_account),
                isError = true,
            )
            throw FollowFailedException(e)
        }
    }.await()

    class FollowFailedException(e: Exception) : Exception(e)
}
