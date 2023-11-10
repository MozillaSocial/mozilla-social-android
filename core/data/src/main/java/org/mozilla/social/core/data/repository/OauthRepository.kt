package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.mastodon.OauthApi

class OauthRepository(private val oauthApi: OauthApi) {

    suspend fun fetchOAuthToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String
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