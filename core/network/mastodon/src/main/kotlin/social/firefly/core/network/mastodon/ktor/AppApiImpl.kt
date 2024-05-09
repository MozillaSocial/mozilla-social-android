package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.parameters
import social.firefly.core.network.mastodon.AppApi
import social.firefly.core.network.mastodon.model.NetworkAccessToken
import social.firefly.core.network.mastodon.model.NetworkAccount
import social.firefly.core.network.mastodon.model.NetworkApplication

class AppApiImpl(
    private val client: HttpClient,
) : AppApi {

    override suspend fun createApplication(
        clientName: String,
        redirectUris: String,
        scopes: String,
        baseUrl: String,
    ): NetworkApplication = client.submitForm(
        url = "https://$baseUrl/api/v1/apps",
        formParameters = parameters {
            append("client_name", clientName)
            append("redirect_uris", redirectUris)
            append("scopes", scopes)
        }
    ).body()

    override suspend fun fetchOAuthToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String,
        baseUrl: String,
    ): NetworkAccessToken = client.submitForm(
        url = "https://$baseUrl/oauth/token",
        formParameters = parameters {
            append("client_id", clientId)
            append("client_secret", clientSecret)
            append("redirect_uri", redirectUri)
            append("code", code)
            append("grant_type", grantType)
        }
    ).body()

    override suspend fun verifyCredentials(
        authHeader: String,
        baseUrl: String,
    ): NetworkAccount = client.get(
        urlString = "https://$baseUrl/api/v1/accounts/verify_credentials"
    ) {
        headers {
            append("Authorization", authHeader)
        }
    }.body()
}