package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.repository.mastodon.VerificationRepository
import timber.log.Timber

class UpdateAllLoggedInAccounts(
    private val verificationRepository: VerificationRepository,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {

    suspend operator fun invoke() = coroutineScope {
        userPreferencesDatastoreManager.dataStores.value.map { dataStore ->
            async {
                val accessToken = dataStore.accessToken.first()
                val domain = dataStore.domain.first()
                try {
                    verificationRepository.verifyUserCredentials(
                        accessToken,
                        domain,
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    null
                }
            }
        }.mapNotNull {
            it.await()
        }.forEach { account ->
            userPreferencesDatastoreManager.dataStores.value.find {
                it.accountId.first() == account.accountId
            }?.apply {
                saveAvatarUrl(account.avatarUrl)
                saveUserName(account.username)
                val defaultLanguage = account.source?.defaultLanguage ?: ""
                saveDefaultLanguage(defaultLanguage)
            }
        }
    }
}