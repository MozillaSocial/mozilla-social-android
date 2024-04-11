package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.usecase.mastodon.R

class UnmuteAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val relationshipRepository: RelationshipRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws UnmuteFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(accountId: String) =
        externalScope.async(dispatcherIo) {
            try {
                relationshipRepository.updateMuted(accountId, false)
                val relationship = accountRepository.unmuteAccount(accountId)
                relationshipRepository.insert(relationship)
            } catch (e: Exception) {
                relationshipRepository.updateMuted(accountId, true)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_unmuting_account),
                    isError = true,
                )
                throw UnmuteFailedException(e)
            }
        }.await()

    class UnmuteFailedException(e: Exception) : Exception(e)
}
