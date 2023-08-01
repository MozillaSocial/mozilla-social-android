package org.mozilla.social.core.network

import okhttp3.MultipartBody
import org.mozilla.social.model.MediaUpdateRequestBody
import org.mozilla.social.model.entity.Account
import org.mozilla.social.model.entity.Attachment
import org.mozilla.social.model.entity.Status
import org.mozilla.social.model.entity.request.StatusCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MastodonApi {

    @GET("/api/v1/accounts/verify_credentials")
    suspend fun verifyAccount() : Account

    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: StatusCreate
    ): Response<Unit>

    @Multipart
    @POST("/api/v2/media")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part,
        @Part("description") description: String? = null,
    ): Attachment

    @PUT("api/v1/media/{mediaId}")
    suspend fun updateMedia(
        @Path("mediaId") mediaId: String,
        @Body requestBody: MediaUpdateRequestBody,
    )

    @GET("/api/v1/timelines/home")
    suspend fun getHomeTimeline(): Response<List<Status>>

    @GET("/api/v1/timelines/public")
    suspend fun getPublicTimeline(): Response<List<Status>>

    @GET("/api/v1/accounts/{accountId}")
    suspend fun getAccount(
        @Path("accountId") accountId: String
    ) : Response<Account>
}