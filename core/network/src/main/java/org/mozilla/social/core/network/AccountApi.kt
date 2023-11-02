package org.mozilla.social.core.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkRelationship
import org.mozilla.social.core.network.model.NetworkStatus
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApi {
    @GET("/api/v1/accounts/{id}")
    suspend fun getAccount(
        @Path("id") accountId: String
    ): NetworkAccount

    @GET("/api/v1/accounts/verify_credentials")
    suspend fun verifyCredentials() : NetworkAccount

    @GET("/api/v1/accounts/{id}/followers")
    suspend fun getAccountFollowers(
        @Path("id") accountId: String,
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkAccount>>

    @GET("/api/v1/accounts/{id}/following")
    suspend fun getAccountFollowing(
        @Path("id") accountId: String,
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
    ): Response<List<NetworkAccount>>

    @GET("/api/v1/accounts/{id}/statuses")
    suspend fun getAccountStatuses(
        @Path("id") accountId: String,
        // Return results older than ID.
        @Query("max_id") olderThanId: String? = null,
        // Return results newer than ID.
        @Query("since_id") newerThanId: String? = null,
        // Return results immediately newer than ID.
        @Query("min_id") immediatelyNewerThanId: String? = null,
        // Maximum number of results to return. Defaults to 20 statuses. Max 40 statuses.
        @Query("limit") limit: Int? = null,
        @Query("only_media") onlyMedia: Boolean = false,
        @Query("exclude_replies") excludeReplies: Boolean = false,
        @Query("exclude_reblogs") excludeBoosts: Boolean = false,
    ): List<NetworkStatus>

    @GET("/api/v1/bookmarks")
    suspend fun getAccountBookmarks(): List<NetworkStatus>

    @GET("/api/v1/favourites")
    suspend fun getAccountFavourites(): List<NetworkStatus>

    @POST("/api/v1/accounts/{accountId}/follow")
    suspend fun followAccount(
        @Path("accountId") accountId: String
    )

    @POST("/api/v1/accounts/{accountId}/unfollow")
    suspend fun unfollowAccount(
        @Path("accountId") accountId: String
    )

    @POST("/api/v1/accounts/{accountId}/block")
    suspend fun blockAccount(
        @Path("accountId") accountId: String,
    )

    @POST("/api/v1/accounts/{accountId}/unblock")
    suspend fun unblockAccount(
        @Path("accountId") accountId: String,
    )

    /**
     * @param duration how long to mute for in seconds.  0 is indefinite
     */
    @FormUrlEncoded
    @POST("/api/v1/accounts/{accountId}/mute")
    suspend fun muteAccount(
        @Path("accountId") accountId: String,
        @Field("duration") duration: Int = 0,
    )

    @POST("/api/v1/accounts/{accountId}/unmute")
    suspend fun unmuteAccount(
        @Path("accountId") accountId: String,
    )

    @GET("/api/v1/accounts/relationships")
    suspend fun getRelationships(
        @Query("id") ids: Array<String>
    ): List<NetworkRelationship>

    @Multipart
    @PATCH("/api/v1/accounts/update_credentials")
    suspend fun updateAccount(
        @Part("display_name") displayName: RequestBody? = null,
        @Part("note") bio: RequestBody? = null,
        @Part("locked") locked: RequestBody? = null,
        @Part("bot") bot: RequestBody? = null,
        @Part avatar: MultipartBody.Part? = null,
        @Part header: MultipartBody.Part? = null,
        @Part("fields_attributes[0][name]") fieldLabel0: RequestBody? = null,
        @Part("fields_attributes[0][value]") fieldContent0: RequestBody? = null,
        @Part("fields_attributes[1][name]") fieldLabel1: RequestBody? = null,
        @Part("fields_attributes[1][value]") fieldContent1: RequestBody? = null,
        @Part("fields_attributes[2][name]") fieldLabel2: RequestBody? = null,
        @Part("fields_attributes[2][value]") fieldContent2: RequestBody? = null,
        @Part("fields_attributes[3][name]") fieldLabel3: RequestBody? = null,
        @Part("fields_attributes[3][value]") fieldContent3: RequestBody? = null,
    ): NetworkAccount
}