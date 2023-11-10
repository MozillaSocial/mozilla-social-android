package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.network.mastodon.AppApi
import org.mozilla.social.model.Application

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
