package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.network.mastodon.OauthApi

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
