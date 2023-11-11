package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.network.mastodon.AppApi
import org.mozilla.social.core.model.Application

class AppRepository(private val appApi: AppApi) {
    suspend fun createApplication(
        clientName: String,
        redirectUris: String,
        scopes: String
    ): Application =
        appApi.createApplication(
            clientName = clientName,
            redirectUris = redirectUris,
            scopes = scopes,
        ).toExternalModel()
}
