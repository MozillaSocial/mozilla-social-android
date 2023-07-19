package org.mozilla.social.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun fetchOAuthToken(
        @Header("domain") domain: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String
    ): AccessToken
}

@Serializable
data class AccessToken(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
)
