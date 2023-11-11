package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel
import org.mozilla.social.core.usecase.mastodon.R
import java.io.File

class UpdateMyAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UpdateAccountFailedException if any error occurred
     */
    suspend operator fun invoke(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null
    ) = externalScope.async(dispatcherIo) {
        try {
            println("johnny 1")
            val updatedAccount = accountRepository.updateAccount(
                displayName = displayName,
                bio = bio,
                locked = locked,
                bot = bot,
                avatar = avatar,
                header = header,
                fields = fields
            )
            println("johnny 2")
            socialDatabase.accountsDao().insert(updatedAccount.toDatabaseModel())
        } catch (e: Exception) {
            println("johnny 3 $e")
            showSnackbar(
                text = StringFactory.resource(R.string.edit_account_save_failed),
                isError = true,
            )
            throw UpdateAccountFailedException(e)
        }
    }.await()

    class UpdateAccountFailedException(e: Exception) : Exception(e)
}