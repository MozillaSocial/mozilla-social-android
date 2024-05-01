package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.usecase.mastodon.R

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
            throw social.firefly.core.usecase.mastodon.account.FollowAccount.FollowFailedException(e)
        }
    }.await()

    class FollowFailedException(e: Exception) : Exception(e)
}
