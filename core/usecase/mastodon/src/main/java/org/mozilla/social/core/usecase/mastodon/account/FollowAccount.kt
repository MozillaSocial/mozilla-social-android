package org.mozilla.social.core.usecase.mastodon.account

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.core.usecase.mastodon.R

class FollowAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws FollowFailedException if any error occurred
     */
    suspend operator fun invoke(
        accountId: String,
        loggedInUserAccountId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.withTransaction {
                socialDatabase.accountsDao().updateFollowingCount(loggedInUserAccountId, 1)
                socialDatabase.relationshipsDao().updateFollowing(accountId, true)
            }
            accountApi.followAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.accountsDao().updateFollowingCount(loggedInUserAccountId, -1)
                socialDatabase.relationshipsDao().updateFollowing(accountId, false)
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