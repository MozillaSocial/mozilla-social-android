package org.mozilla.social.core.domain.account

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.AccountApi

class UnfollowAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UnfollowFailedException if any error occurred
     */
    suspend operator fun invoke(
        accountId: String,
        loggedInUserAccountId: String,
    ) = externalScope.async(dispatcherIo) {
        var timelinePosts: List<HomeTimelineStatus>? = null
        try {
            socialDatabase.withTransaction {
                timelinePosts = socialDatabase.homeTimelineDao().getPostsFromAccount(accountId)
                socialDatabase.homeTimelineDao().removePostsFromAccount(accountId)
                socialDatabase.accountsDao().updateFollowingCount(loggedInUserAccountId, -1)
                socialDatabase.relationshipsDao().updateFollowing(accountId, false)
            }
            accountApi.unfollowAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                timelinePosts?.let { socialDatabase.homeTimelineDao().insertAll(it) }
                socialDatabase.accountsDao().updateFollowingCount(loggedInUserAccountId, 1)
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