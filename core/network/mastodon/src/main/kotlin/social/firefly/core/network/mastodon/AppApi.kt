package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkAccessToken
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkApplication

interface AppApi {

    suspend fun createApplication(
        clientName: String,
        redirectUris: String,
        scopes: String,
        baseUrl: String,
    ): NetworkApplication

    suspend fun fetchOAuthToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String,
        baseUrl: String,
    ): NetworkAccessToken

    suspend fun verifyCredentials(
        authHeader: String,
        baseUrl: String,
    ): NetworkAccount
}
