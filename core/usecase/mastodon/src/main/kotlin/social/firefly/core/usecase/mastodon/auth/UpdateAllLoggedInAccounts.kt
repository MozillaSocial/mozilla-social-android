package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.repository.mastodon.VerificationRepository

class UpdateAllLoggedInAccounts(
    private val verificationRepository: VerificationRepository,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {

    suspend operator fun invoke() = coroutineScope {
        userPreferencesDatastoreManager.dataStores.value.map { dataStore ->
            async {
                val accessToken = dataStore.accessToken.first()
                val domain = dataStore.domain.first()
                verificationRepository.verifyUserCredentials(
                    accessToken,
                    domain,
                )
            }
        }.map {
            it.await()
        }.forEach { account ->
            userPreferencesDatastoreManager.dataStores.value.find {
                it.accountId.first() == account.accountId
            }?.apply {
                saveAvatarUrl(account.avatarUrl)
                saveUserName(account.displayName)
            }
        }
    }
}