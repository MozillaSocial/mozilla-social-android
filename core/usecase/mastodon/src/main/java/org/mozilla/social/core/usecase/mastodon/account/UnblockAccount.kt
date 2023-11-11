package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.core.usecase.mastodon.R

class UnblockAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UnblockFailedException if any error occurred
     */
    suspend operator fun invoke(
        accountId: String
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.relationshipsDao().updateBlocked(accountId, false)
            accountApi.unblockAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateBlocked(accountId, true)
            showSnackbar(
                text = StringFactory.resource(R.string.error_unblocking_account),
                isError = true,
            )
            throw UnblockFailedException(e)
        }
    }.await()

    class UnblockFailedException(e: Exception) : Exception(e)
}