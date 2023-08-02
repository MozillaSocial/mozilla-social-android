package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.OauthApi

class AuthRepository(private val oauthApi: OauthApi) {

    suspend fun fetchOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String
    ): String {
        return oauthApi.fetchOAuthToken(
            domain = domain,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            code = code,
            grantType = grantType,
        ).accessToken
    }
}