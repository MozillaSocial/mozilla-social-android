package social.firefly.core.repository.mastodon

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.network.mastodon.OauthApi

class OauthRepository(private val oauthApi: OauthApi) {
    @PreferUseCase
    suspend fun fetchOAuthToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String,
    ): String {
        return oauthApi.fetchOAuthToken(
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            code = code,
            grantType = grantType,
        ).accessToken
    }
}
