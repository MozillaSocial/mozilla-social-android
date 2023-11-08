package org.mozilla.social.core.domain.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.AccountApi

class MuteAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        accountId: String
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.homeTimelineDao().removePostsFromAccount(accountId)
            socialDatabase.localTimelineDao().removePostsFromAccount(accountId)
            socialDatabase.federatedTimelineDao().removePostsFromAccount(accountId)
            socialDatabase.relationshipsDao().updateMuted(accountId, true)
            accountApi.muteAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateMuted(accountId, false)
            showSnackbar(
                text = StringFactory.resource(R.string.error_muting_account),
                isError = true,
            )
            throw MuteFailedException(e)
        }
    }.await()

    class MuteFailedException(e: Exception) : Exception(e)
}