package org.mozilla.social.core.network

import okhttp3.MultipartBody
import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkAttachment
import org.mozilla.social.core.network.model.NetworkSearchResult
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.core.network.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.model.request.NetworkStatusCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MastodonApi {

    @GET("/api/v1/accounts/verify_credentials")
    suspend fun verifyAccount() : NetworkAccount

    @POST("api/v1/statuses")
    suspend fun postStatus(
        @Body status: NetworkStatusCreate
    ): Response<Unit>

    @Multipart
    @POST("/api/v2/media")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part,
        @Part("description") description: String? = null,
    ): NetworkAttachment

    @PUT("api/v1/media/{mediaId}")
    suspend fun updateMedia(
        @Path("mediaId") mediaId: String,
        @Body requestBody: NetworkMediaUpdate,
    )

    @GET("/api/v1/timelines/home")
    suspend fun getHomeTimeline(): List<NetworkStatus>

    @GET("/api/v1/timelines/public")
    suspend fun getPublicTimeline(): List<NetworkStatus>

    @GET("/api/v2/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("type") type: String,
    ) : NetworkSearchResult

    @GET("/api/v1/accounts/{id}")
    suspend fun getAccount(
        @Path("id") accountId: String
    ): NetworkAccount

    @GET("/api/v1/accounts/{id}/followers")
    suspend fun getAccountFollowers(
        @Path("id") accountId: String
    ): List<NetworkAccount>

    @GET("/api/v1/accounts/{id}/following")
    suspend fun getAccountFollowing(
        @Path("id") accountId: String
    ): List<NetworkAccount>

    @GET("/api/v1/accounts/{id}/statuses")
    suspend fun getAccountStatuses(
        @Path("id") accountId: String
    ): List<NetworkStatus>

    @GET("/api/v1/bookmarks")
    suspend fun getAccountBookmarks(): List<NetworkStatus>

    @GET("/api/v1/favourites")
    suspend fun getAccountFavourites(): List<NetworkStatus>
}