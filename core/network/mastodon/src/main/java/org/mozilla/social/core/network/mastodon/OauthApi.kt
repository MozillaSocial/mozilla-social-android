package org.mozilla.social.core.network.mastodon

import org.mozilla.social.core.network.mastodon.model.NetworkAccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OauthApi {
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun fetchOAuthToken(
//        @Header("domain") domain: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String,
    ): NetworkAccessToken
}
