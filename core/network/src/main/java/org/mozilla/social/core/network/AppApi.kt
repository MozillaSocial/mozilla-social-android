package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkApplication
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AppApi {
    @FormUrlEncoded
    @POST("/api/v1/apps")
    suspend fun createApplication(
        @Field("client_name") clientName: String,
        @Field("redirect_uris") redirectUris: String,
        @Field("scopes") scopes: String,
    ): NetworkApplication
}