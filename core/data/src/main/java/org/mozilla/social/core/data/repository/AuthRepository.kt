package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.AuthService

class AuthRepository(private val service: AuthService) {

    suspend fun fetchOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
        grantType: String
    ): String {
        return service.fetchOAuthToken(
            domain = domain,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            code = code,
            grantType = grantType,
        ).accessToken
    }
}