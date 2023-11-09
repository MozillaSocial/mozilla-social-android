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

class UnmuteAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UnmuteFailedException if any error occurred
     */
    suspend operator fun invoke(
        accountId: String
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.relationshipsDao().updateMuted(accountId, false)
            accountApi.unmuteAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateMuted(accountId, true)
            showSnackbar(
                text = StringFactory.resource(R.string.error_unmuting_account),
                isError = true,
            )
            throw UnmuteFailedException(e)
        }
    }.await()

    class UnmuteFailedException(e: Exception) : Exception(e)
}