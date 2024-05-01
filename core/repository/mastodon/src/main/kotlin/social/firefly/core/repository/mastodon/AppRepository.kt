package social.firefly.core.repository.mastodon

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.Application
import social.firefly.core.network.mastodon.AppApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class AppRepository(private val appApi: AppApi) {
    @PreferUseCase
    suspend fun createApplication(
        clientName: String,
        redirectUris: String,
        scopes: String,
    ): Application =
        appApi.createApplication(
            clientName = clientName,
            redirectUris = redirectUris,
            scopes = scopes,
        ).toExternalModel()
}
