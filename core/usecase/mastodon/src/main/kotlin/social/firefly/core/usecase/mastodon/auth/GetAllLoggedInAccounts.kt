package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.model.Account
import social.firefly.core.repository.mastodon.VerificationRepository

class GetAllLoggedInAccounts(
    private val verificationRepository: VerificationRepository,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {

    suspend operator fun invoke(): List<Account> = coroutineScope {
        userPreferencesDatastoreManager.dataStores.value.map { dataStore ->
            async {
                val accessToken = dataStore.accessToken.first()
                verificationRepository.verifyUserCredentials(
                    accessToken,
                    ""
                )
            }
        }.map {
            it.await()
        }
    }
}