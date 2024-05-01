package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.usecase.mastodon.R
import java.io.File

class UpdateMyAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws UpdateAccountFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null,
    ) = externalScope.async(dispatcherIo) {
        try {
            val updatedAccount =
                accountRepository.updateAccount(
                    displayName = displayName,
                    bio = bio,
                    locked = locked,
                    bot = bot,
                    avatar = avatar,
                    header = header,
                    fields = fields,
                )
            accountRepository.insert(updatedAccount)
        } catch (e: Exception) {
            showSnackbar(
                text = StringFactory.resource(R.string.edit_account_save_failed),
                isError = true,
            )
            throw UpdateAccountFailedException(e)
        }
    }.await()

    class UpdateAccountFailedException(e: Exception) : Exception(e)
}
