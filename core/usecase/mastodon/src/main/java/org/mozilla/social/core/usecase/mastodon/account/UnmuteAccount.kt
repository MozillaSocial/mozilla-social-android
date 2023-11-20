package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.R

class UnmuteAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws UnmuteFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(accountId: String) =
        externalScope.async(dispatcherIo) {
            try {
                socialDatabase.relationshipsDao().updateMuted(accountId, false)
                val relationship = accountRepository.unmuteAccount(accountId)
                relationshipRepository.insert(relationship)
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
