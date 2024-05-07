package social.firefly.core.repository.mastodon

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.Account
import social.firefly.core.model.Application
import social.firefly.core.network.mastodon.AppApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class VerificationRepository(
    private val appApi: AppApi
) {
    @PreferUseCase
    suspend fun createApplication(
        clientName: String,
        redirectUris: String,
        scopes: String,
        baseUrl: String,
    ): Application = appApi.createApplication(
        clientName = clientName,
        redirectUris = redirectUris,
        scopes = scopes,
        baseUrl = baseUrl,
    ).toExternalModel()

    @PreferUseCase
    suspend fun fetchOAuthToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String,
        baseUrl: String,
    ): String = appApi.fetchOAuthToken(
        clientId = clientId,
        clientSecret = clientSecret,
        redirectUri = redirectUri,
        code = code,
        grantType = grantType,
        baseUrl = baseUrl,
    ).accessToken

    suspend fun verifyUserCredentials(
        accessToken: String,
        baseUrl: String,
    ): Account = appApi.verifyCredentials(
        authHeader = "Bearer $accessToken",
        baseUrl = baseUrl,
    ).toExternalModel()
}