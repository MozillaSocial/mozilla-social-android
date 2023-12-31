package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.Application
import org.mozilla.social.core.network.mastodon.AppApi
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

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
