package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.R

class UnblockAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws UnblockFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(accountId: String) =
        externalScope.async(dispatcherIo) {
            try {
                relationshipRepository.updateBlocked(accountId, false)
                val relationship = accountRepository.unblockAccount(accountId)
                relationshipRepository.insert(relationship)
            } catch (e: Exception) {
                relationshipRepository.updateBlocked(accountId, true)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_unblocking_account),
                    isError = true,
                )
                throw UnblockFailedException(e)
            }
        }.await()

    class UnblockFailedException(e: Exception) : Exception(e)
}
