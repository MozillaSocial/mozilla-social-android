package social.firefly.core.network.mastodon

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import social.firefly.core.network.mastodon.model.NetworkAccount
import social.firefly.core.network.mastodon.model.NetworkApplication

interface AppApi {

    @FormUrlEncoded
    @POST("/api/v1/apps")
    suspend fun createApplication(
        @Field("client_name") clientName: String,
        @Field("redirect_uris") redirectUris: String,
        @Field("scopes") scopes: String,
    ): NetworkApplication

    @GET("/api/v1/accounts/verify_credentials")
    suspend fun verifyCredentials(
        @Header("Authorization") authHeader: String,
    ): NetworkAccount
}
